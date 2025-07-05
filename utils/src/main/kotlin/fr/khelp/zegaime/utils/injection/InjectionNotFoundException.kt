package fr.khelp.zegaime.utils.injection

class InjectionNotFoundException(name : String) : Exception("$name not injected")
