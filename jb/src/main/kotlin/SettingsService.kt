package com.codestream

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.extensions.PluginId
import protocols.agent.Extension
import protocols.agent.Ide
import protocols.agent.TraceLevel
import protocols.webview.CodeStreamEnvironment
import protocols.webview.Configs

const val INLINE_CODEMARKS = "viewCodemarksInline"

// PD urls
private const val API_PD = "https://pd-api.codestream.us:9443"
private const val WEB_PD = "http://pd-app.codestream.us:1380"

// QA urls
private const val API_QA = "https://qa-api.codestream.us"
private const val WEB_QA = "http://qa-app.codestream.us"

// PROD urls
private const val API_PROD = "https://api.codestream.com"
private const val WEB_PROD = "https://app.codestream.com"

data class SettingsServiceState(
    var autoSignIn: Boolean = true,
    var email: String? = null,
    var serverUrl: String = API_PROD,
    var webAppUrl: String = WEB_PROD,
    var avatars: Boolean = true,
    var muteAll: Boolean = false,
    var team: String? = null,
    var showFeedbackSmiley: Boolean = true,
    var webViewConfig: MutableMap<String, String?> = mutableMapOf(
        INLINE_CODEMARKS to "true"
    )
)

@State(name = "CodeStream", storages = [Storage("codestream.xml")])
class SettingsService : PersistentStateComponent<SettingsServiceState> {
    private var _state = SettingsServiceState()

    override fun getState(): SettingsServiceState = _state

    override fun loadState(state: SettingsServiceState) {
        _state = state
    }

    private val viewCodemarksInline: Boolean
        get() {
            return state.webViewConfig[INLINE_CODEMARKS]?.toBoolean() ?: true
        }

    val environment: CodeStreamEnvironment
        get() = CodeStreamEnvironment.PROD
    val environmentVersion: String
        get() = PluginManager.getPlugin(PluginId.findId("com.codestream.jetbrains-codestream"))!!.version
    val extensionInfo: Extension
        get() {
            return Extension()
        }
    val ideInfo: Ide
        get() {
            return Ide()
        }
    val traceLevel: TraceLevel
        get() {
            return TraceLevel.DEBUG
        }
    val isDebugging: Boolean
        get() {
            return true
        }
    var currentStreamId: String? = null
    var threadId: String? = null

    val team
        get() = state.team

    fun getWebviewConfigs(): Configs = Configs(
        state.serverUrl,
        state.email,
        state.avatars,
        viewCodemarksInline,
        state.muteAll,
        isDebugging,
        state.showFeedbackSmiley
    )

    fun getEnvironmentDisplayPrefix(): String {
        return when(state.serverUrl) {
            API_PD -> "PD:"
            API_QA -> "QA:"
            else -> if (state.serverUrl.contains("localhost")) {
                "Local:"
            } else {
                "CodeStream:"
            }
        }
    }

    // 💩: I HATE THIS
    fun set(name: String, value: String?) {
        if (state.webViewConfig.containsKey(name)) {
            state.webViewConfig[name] = value
        } else {
            when (name) {
                "muteAll" -> value?.let {
                    state.muteAll = it.toBoolean()
                }
            }
        }
    }
}
