package com.codestream

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Consumer
import java.awt.Component
import java.awt.event.MouseEvent

class CodeStreamStatusBarWidget(val project: Project) : StatusBarWidget, StatusBarWidget.TextPresentation,
    ServiceConsumer(project) {

    init {
        sessionService.onUserLoggedInChanged { refresh() }
        sessionService.onUnreadsChanged { refresh() }
    }

    fun refresh() {
        val statusBar = WindowManager.getInstance().getIdeFrame(null).statusBar
        statusBar?.updateWidget(ID())
    }

    override fun ID() = "CodeStream.StatusBar"

    override fun getPresentation(type: StatusBarWidget.PlatformType) = this

    override fun install(statusBar: StatusBar) = Unit

    override fun dispose() = Unit

    override fun getTooltipText() = "Click to open CodeStream"

    override fun getClickConsumer() = Consumer<MouseEvent> {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("CodeStream")
        with(toolWindow) {
            when (isVisible) {
                true -> hide(null)
                false -> show(null)
            }
        }
    }

    override fun getText(): String {
        val prefix = settingsService.getEnvironmentDisplayPrefix()

        val userLoggedIn = sessionService.userLoggedIn ?: return "$prefix Sign in..."
        val username = if (userLoggedIn.teamsCount == 1) {
            userLoggedIn.user.username
        } else {
            userLoggedIn.user.username + " - " + userLoggedIn.team.name
        }

        val suffix = when (sessionService.unreads) {
            0 -> ""
            in 1..19 -> "(${sessionService.unreads})"
            else -> "(20+)"
        }

        return "$prefix $username $suffix"
    }

    override fun getAlignment(): Float {
        return Component.RIGHT_ALIGNMENT
    }

}
