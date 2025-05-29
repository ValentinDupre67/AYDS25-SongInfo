package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.data.RepositoryInyector

object PresenterInyector {

    val view : View = ViewImpl()

    private lateinit var presenter : Presenter
    fun init(view: View) {
        RepositoryInyector.init(view)

        val repositoy = RepositoryInyector.getRepository()

        presenter = PresenterImpl(repositoy)
    }
}