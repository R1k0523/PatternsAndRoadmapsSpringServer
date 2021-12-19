package boring.owl.parapp.service

import org.springframework.web.multipart.MultipartFile
import boring.owl.parapp.entities.exceptions.FileStorageException
import java.nio.file.StandardCopyOption
import java.io.IOException
import boring.owl.parapp.entities.exceptions.MyFileNotFoundException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.lang.Exception
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileStorageService {
    private val fileStorageLocation: Path
    fun storeFile(file: MultipartFile): String {
        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        return try {
            if (fileName.contains("..")) {
                throw FileStorageException("Sorry! Filename contains invalid path sequence $fileName")
            }
            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(
                file.inputStream,
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
            )
            fileName
        } catch (ex: IOException) {
            throw FileStorageException(
                "Could not store file $fileName. Please try again!",
                ex
            )
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw MyFileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw MyFileNotFoundException("File not found $fileName", ex)
        }
    }

    init {
        val path = "/posts/uploads"
        fileStorageLocation = Paths.get(path)
            .toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (ex: Exception) {
            throw FileStorageException(
                "Could not create the directory where the uploaded files will be stored.",
                ex
            )
        }
    }
}