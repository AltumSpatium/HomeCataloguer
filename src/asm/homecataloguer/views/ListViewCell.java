package asm.homecataloguer.views;

import asm.homecataloguer.core.CatalogFile;

import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<CatalogFile>
{
    @Override
    public void updateItem(CatalogFile catalogFile, boolean empty)
    {
        super.updateItem(catalogFile, empty);
        if (catalogFile != null)
        {
            Data data = new Data();
            data.setInfo(catalogFile);
            setGraphic(data.getBox());
        }
        else setGraphic(null);
    }
}
