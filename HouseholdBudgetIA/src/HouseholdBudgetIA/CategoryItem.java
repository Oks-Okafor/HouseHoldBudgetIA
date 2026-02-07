package HouseholdBudgetIA;

/*
 CategoryItem
 Simple data class used to represent a category
 when transferring data between the database
 and the GUI.
*/
public class CategoryItem {

    private int categoryID;
    private String categoryName;

    public CategoryItem(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}

