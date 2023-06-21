package stepdefinitions;

import enums.Pages;
import helper.ScenarioContext;

import org.openqa.selenium.support.PageFactory;

public class PageTracker {

    /**
     * Configure the {@link ScenarioContext#currentPage} to be {@param page}
     * and update the {@link ScenarioContext#previousPage} appropriately based
     * on this configuration.  If a {@link Pages#pageObject} has not already
     * been instantiated for this {@param page} before then it will be
     * instantiated for it.
     *
     * @see ScenarioContext
     * @see Pages
     * @param page
     *         - The {@link Pages} object to configure as the current page.
     */
    public static void setPage(Pages page){
        //If page supplied is null, throw exception
        if(page == null){
            throw new RuntimeException("Page is null/has not been configured");
        }

        /* Set previousPage, except for when execution has only just started
        and when page is the same as the ScenarioContext.currentPage already set */
        ScenarioContext.previousPage =
                ScenarioContext.currentPage == null || page != ScenarioContext.currentPage
                        ? ScenarioContext.currentPage
                        : ScenarioContext.previousPage;

        //Set page as currentPage
        ScenarioContext.currentPage = page;

        //Instantiate object for this specific page (if needed/not done already)
        if(page.getPageObject() == null) {
            page.setPageObject(
                    PageFactory.initElements(
                            ScenarioContext.driver,
                            page.getClassForPage())
            );
        }
    }

    public static void setPage(String pageRef){
        setPage(Pages.getPagesEnumFromPageRef(pageRef));
    }

}
