package com.anemoi.itr.storage;

import java.io.Serializable;
import java.util.ArrayList;

public class GenRowData implements Serializable {

	int projectId;
	String tableName;
	int rowNum;

	String v1;
	String v2;
	String v3;
	String v4;
	String v5;
	String v6;
	String v7;
	String v8;
	String v9;	
	String v10;
	String v11;
	String v12;
	String v13;
	String v14;
	String v15;
	String v16;
	String v17;
	String v18;
	String v19;
	String v20;

	public GenRowData() {

	}

	public GenRowData(int pid, String table , int rowid ) {
		this.projectId = pid ;
		this.tableName = table ;
		this.rowNum = rowid ;
	}

	public void setRow(ArrayList<String> d)
		{		
		System.out.print("\n ROW SIZE : " + d.size() );
		if(d.size() > 0) v1 = d.get(0);
		if(d.size() > 1) v2 = d.get(1);
		if(d.size() > 2) v3 = d.get(2);
		if(d.size() > 3) v4 = d.get(3);
		if(d.size() > 4) v5 = d.get(4);
		if(d.size() > 5) v6 = d.get(5);
		if(d.size() > 6) v7 = d.get(6);
		if(d.size() > 7) v8 = d.get(7);
		if(d.size() > 8) v9 = d.get(8);
		if(d.size() > 9) v10 = d.get(9);
		if(d.size() > 10) v11 = d.get(10);
		if(d.size() > 11) v12 = d.get(11);
		if(d.size() > 12) v13 = d.get(12);
		if(d.size() > 13) v14 = d.get(13);
		if(d.size() > 14) v15 = d.get(14);
		if(d.size() > 15) v16 = d.get(15);
		if(d.size() > 16) v17 = d.get(16);
		if(d.size() > 17) v18 = d.get(17);
		if(d.size() > 18) v19 = d.get(18);
		if(d.size() > 19) v20 = d.get(19);
		}

	
	
	public ArrayList<String> getList() {
		ArrayList<String> dd = new ArrayList<String>();

		dd.add(v1);
		if (v2 == null)
			return dd;

		dd.add(v2);
		if (v2 == null)
			return dd;

		dd.add(v3);
		if (v2 == null)
			return dd;

		dd.add(v4);
		if (v2 == null)
			return dd;

		dd.add(v5);
		if (v2 == null)
			return dd;

		dd.add(v6);
		if (v2 == null)
			return dd;

		dd.add(v7);
		if (v2 == null)
			return dd;

		dd.add(v8);
		if (v2 == null)
			return dd;

		dd.add(v9);
		if (v2 == null)
			return dd;

		dd.add(v10);
		if (v2 == null)
			return dd;

		dd.add(v11);
		if (v2 == null)
			return dd;

		dd.add(v12);
		if (v2 == null)
			return dd;

		dd.add(v13);
		if (v2 == null)
			return dd;

		dd.add(v14);
		if (v2 == null)
			return dd;

		dd.add(v15);
		if (v2 == null)
			return dd;

		dd.add(v16);
		if (v2 == null)
			return dd;

		dd.add(v17);
		if (v2 == null)
			return dd;

		dd.add(v18);
		if (v2 == null)
			return dd;

		dd.add(v19);
		if (v2 == null)
			return dd;

		dd.add(v19);
		if (v2 == null)
			return dd;

		return dd;
	}

	public ArrayList<String> getList(int rowSize) {
		ArrayList<String> dd = new ArrayList<String>();
		int i = 0;
		dd.add(v1);
		if (++i >= rowSize)
			return dd;

		dd.add(v2);
		if (++i >= rowSize)
			return dd;

		dd.add(v3);
		if (++i >= rowSize)
			return dd;

		dd.add(v4);
		if (++i >= rowSize)
			return dd;

		dd.add(v5);
		if (++i >= rowSize)
			return dd;

		dd.add(v6);
		if (++i >= rowSize)
			return dd;

		dd.add(v7);
		if (++i >= rowSize)
			return dd;

		dd.add(v8);
		if (++i >= rowSize)
			return dd;

		dd.add(v9);
		if (++i >= rowSize)
			return dd;

		dd.add(v10);
		if (++i >= rowSize)
			return dd;

		dd.add(v11);
		if (++i >= rowSize)
			return dd;

		dd.add(v12);
		if (++i >= rowSize)
			return dd;

		dd.add(v13);
		if (++i >= rowSize)
			return dd;

		dd.add(v14);
		if (++i >= rowSize)
			return dd;

		dd.add(v15);
		if (++i >= rowSize)
			return dd;

		dd.add(v16);
		if (++i >= rowSize)
			return dd;

		dd.add(v17);
		if (++i >= rowSize)
			return dd;

		dd.add(v18);
		if (++i >= rowSize)
			return dd;

		dd.add(v19);
		if (++i >= rowSize)
			return dd;

		dd.add(v20);

		return dd;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getV9() {
		return v9;
	}

	public void setV9(String v9) {
		this.v9 = v9;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public String getV3() {
		return v3;
	}

	public void setV3(String v3) {
		this.v3 = v3;
	}

	public String getV4() {
		return v4;
	}

	public void setV4(String v4) {
		this.v4 = v4;
	}

	public String getV5() {
		return v5;
	}

	public void setV5(String v5) {
		this.v5 = v5;
	}

	public String getV6() {
		return v6;
	}

	public void setV6(String v6) {
		this.v6 = v6;
	}

	public String getV7() {
		return v7;
	}

	public void setV7(String v7) {
		this.v7 = v7;
	}

	public String getV8() {
		return v8;
	}

	public void setV8(String v8) {
		this.v8 = v8;
	}

	public String getV10() {
		return v10;
	}

	public void setV10(String v10) {
		this.v10 = v10;
	}

	public String getV11() {
		return v11;
	}

	public void setV11(String v11) {
		this.v11 = v11;
	}

	public String getV12() {
		return v12;
	}

	public void setV12(String v12) {
		this.v12 = v12;
	}

	public String getV13() {
		return v13;
	}

	public void setV13(String v13) {
		this.v13 = v13;
	}

	public String getV14() {
		return v14;
	}

	public void setV14(String v14) {
		this.v14 = v14;
	}

	public String getV15() {
		return v15;
	}

	public void setV15(String v15) {
		this.v15 = v15;
	}

	public String getV16() {
		return v16;
	}

	public void setV16(String v16) {
		this.v16 = v16;
	}

	public String getV17() {
		return v17;
	}

	public void setV17(String v17) {
		this.v17 = v17;
	}

	public String getV18() {
		return v18;
	}

	public void setV18(String v18) {
		this.v18 = v18;
	}

	public String getV19() {
		return v19;
	}

	public void setV19(String v19) {
		this.v19 = v19;
	}

	public String getV20() {
		return v20;
	}

	public void setV20(String v20) {
		this.v20 = v20;
	}

}
