package be.vanlooverenkoen.midimacro.model;

/**
 * BusinessEntity that represents a Licence for a libary used in the app
 *
 * @author Koen Van Looveren
 * @since 1.0
 */
public class Licence {

    //region Variables
    private String productName;
    private String license;
    private String gitUrl;
    //endregion

    //region Constructor
    public Licence(String productName, String license, String gitUrl) {
        this.productName = productName;
        this.license = license;
        this.gitUrl = gitUrl;
    }
    //endregion

    //region GETTERS
    public String getProductName() {
        return productName;
    }

    public String getLicense() {
        return license;
    }

    public String getGitUrl() {
        return gitUrl;
    }
    //endregion
}
