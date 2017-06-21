package asm.homecataloguer.views;

import asm.homecataloguer.models.CatalogItem;
import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<CatalogItem>
{
    @Override
    public void updateItem(CatalogItem catalogItem, boolean empty)
    {
        super.updateItem(catalogItem, empty);
        if (catalogItem != null)
        {
            Data data = new Data();
            data.setInfo(catalogItem);
            setGraphic(data.getBox());
        }
        else setGraphic(null);
    }
}
