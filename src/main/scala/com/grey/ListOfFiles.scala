package com.grey

import java.io.File

class ListOfFiles {

  def listOfFiles(dataDirectory: String, listOfExtensions: List[String]): List[File] = {

    val listOfFilesObject = new File(dataDirectory)
    val listOfFiles: List[File] = if (listOfFilesObject.exists() && listOfFilesObject.isDirectory) {
      listOfFilesObject.listFiles.filter(x => x.isFile).toList
    } else {
      List[File]()
    }

    listOfFiles.filter{ fileName =>
      listOfExtensions.exists(extensionString => fileName.getName.endsWith(extensionString))
    }

  }

}
