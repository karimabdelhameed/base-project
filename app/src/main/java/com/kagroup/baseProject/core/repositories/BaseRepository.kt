package com.kagroup.baseProject.core.repositories

import com.custom.ResponseHandler
import com.kagroup.baseProject.core.source.remote.WebService
import javax.inject.Inject


open class BaseRepository  {

    @Inject
    lateinit var webService : WebService

    @Inject
    lateinit var responseHandler: ResponseHandler


}