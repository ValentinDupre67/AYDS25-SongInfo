package ayds.songinfo.moredetails.fulllogic.presentation

object ViewInyector {

    val view : View = ViewImpl()
    fun init(){
        PresenterInyector.init(this.view)
    }
}