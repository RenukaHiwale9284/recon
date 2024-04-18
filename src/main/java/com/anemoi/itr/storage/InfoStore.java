package com.anemoi.itr.storage;

import java.io.File;

import com.anemoi.itr.ITR6DataSet;
import com.anemoi.itr.SheetTemplate;
import com.anemoi.itr.TableConfiguration;
import com.anemoi.itr.TaxListElement;
import com.anemoi.itr.TemplateConf;

public interface InfoStore {

	void loadTemplateConf(TemplateConf templateConf) throws Exception;

	void loadData(ITR6DataSet itr6DataSet) throws Exception;

	File getITR6File() throws Exception;

	void loadStructure(SheetTemplate sheetTemplate) throws Exception;

	GenDataTable fetchDataTable(String table, TableConfiguration tc);

	void storeTable(TaxListElement listElement, TableConfiguration tc);

}
