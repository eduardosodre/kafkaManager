package br.com.kafkamanager.infrastructure.swing.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

@Getter
public class MyTableModel<E> extends DefaultTableModel {

	private Class<E> clazz;
    private final List<Field> fields;
    private List<E> data = new ArrayList<>();

    public MyTableModel(List<String> fieldNames, Class<E> clazz) {
        super(new Object[][] {}, fieldNames.toArray());
        this.clazz = clazz;
        this.fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                fields.add(field);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
        
		addTableModelListener(e -> {
			int row = e.getFirstRow();
			int col = e.getColumn();
			if (row >= 0 && col >= 0) {
				Object value = getValueAt(row, col);
				updateObjectValue(row, col, value);
			}
		});
    }

    public void add(E obj) {
        data.add(obj);
        Object[] row = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            try {
                row[i] = fields.get(i).get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        addRow(row);
    }

    public E get(int row) throws IndexOutOfBoundsException {
        return data.get(row);
    }
    
    public void remove(int row) {
        removeRow(row);
        data.remove(row);
    }

    public void setData(List<E> data) {
    	this.data = new ArrayList<>();
    	this.data.addAll(data);
        setRowCount(0);
        for (E obj : data) {
            add(obj);
        }
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
        updateObjectValue(row, col, value);
    }
    private void updateObjectValue(int row, int col, Object value) {
        E obj = data.get(row);
        Field field = fields.get(0);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
        	e.printStackTrace();
        }
    }
}

