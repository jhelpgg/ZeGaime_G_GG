package fr.khelp.zegaime.resources

import fr.khelp.zegaime.utils.extensions.regularExpression
import fr.khelp.zegaime.utils.io.readLines
import fr.khelp.zegaime.utils.regex.ANY
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import java.util.Locale

class ResourcesText internal constructor(private var basePath : String, val resources : Resources)
{
    companion object
    {
        private const val TEXT_SEPARATOR = "-<=-=>-"
        private const val START_COMMENT = "#-<*"
        private const val END_COMMENT = "*>-#"
        private const val STANDARD_TEXT_HEADER = "<Text>"
        private const val STANDARD_TEXT_FOOTER = "</Text>"
        private val TEXT_GROUP = ANY.zeroOrMore()
            .group()
        private val TEXT_REGEX =
            ResourcesText.STANDARD_TEXT_HEADER.regularExpression + ResourcesText.TEXT_GROUP + ResourcesText.STANDARD_TEXT_FOOTER.regularExpression

        fun standardTextKey(text : String) : String =
            "${ResourcesText.STANDARD_TEXT_HEADER}$text${ResourcesText.STANDARD_TEXT_FOOTER}"
    }

    private val observableSource = ObservableSource(this)
    private val texts = HashMap<String, String>()

    val observableChange = this.observableSource.observable

    init
    {
        Resources.languageObservableData.observable.register(this::loadTexts)
    }

    operator fun get(key : String) : String
    {
        val matcher = ResourcesText.TEXT_REGEX.matcher(key)

        if (matcher.matches())
        {
            return matcher.group(ResourcesText.TEXT_GROUP)
        }

        return synchronized(this.texts) {
            this.texts[key] ?: if (this != defaultTexts) defaultTexts[key] else "/!\\ No key defined for $key /!\\"
        }
    }

    private fun loadTexts(locale : Locale)
    {
        synchronized(this.texts) { this.texts.clear() }

        this.loadText(this.basePath)
        val language = locale.language

        if (language.isNotEmpty())
        {
            this.loadText("${this.basePath}_$language")

            val country = locale.country

            if (country.isNotEmpty())
            {
                this.loadText("${this.basePath}_${language}_$country")

                val variant = locale.variant

                if (variant.isNotEmpty())
                {
                    this.loadText("${this.basePath}_${language}_${country}_$variant")
                }
            }
        }

        this.observableSource.value = this
    }

    private fun loadText(path : String)
    {
        var comment = false
        var keyRead = false
        var notFirstLine = false
        var key = ""
        var text = StringBuilder()

        readLines({ this.resources.inputStream(path) },
                  { line ->
                      val lineTrim = line.trim()

                      when (lineTrim)
                      {
                          ResourcesText.START_COMMENT  -> comment = true
                          ResourcesText.END_COMMENT    -> comment = false
                          ResourcesText.TEXT_SEPARATOR ->
                              if (keyRead && !comment)
                              {
                                  synchronized(this.texts)
                                  {
                                      this.texts[key] = this.resources.replaceResourcesLinkIn(text.toString())
                                  }

                                  keyRead = false
                                  key = ""
                              }

                          else                         ->
                              if (!comment)
                              {
                                  if (!keyRead && lineTrim.isNotEmpty())
                                  {
                                      keyRead = true
                                      key = lineTrim
                                      notFirstLine = false
                                      text = StringBuilder()
                                  }
                                  else if (keyRead)
                                  {
                                      if (notFirstLine)
                                      {
                                          text.append('\n')
                                      }

                                      text.append(line)
                                      notFirstLine = true
                                  }
                              }
                      }
                  },
                  {})
    }
}
