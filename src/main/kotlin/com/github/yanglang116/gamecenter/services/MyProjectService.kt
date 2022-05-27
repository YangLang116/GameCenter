package com.github.yanglang116.gamecenter.services

import com.intellij.openapi.project.Project
import com.github.yanglang116.gamecenter.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
