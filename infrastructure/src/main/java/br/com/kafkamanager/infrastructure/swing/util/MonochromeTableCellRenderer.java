package br.com.kafkamanager.infrastructure.swing.util;

import static br.com.kafkamanager.infrastructure.swing.util.SetupColor.darkenColor;
import static br.com.kafkamanager.infrastructure.swing.util.SetupColor.lightenColor;
import static com.formdev.flatlaf.FlatLaf.isLafDark;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class MonochromeTableCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        final var colorBackground = UIManager.getColor("Table.background");
        final var colorForeground = UIManager.getColor("Table.foreground");
        final var oddBackgroundRowColor = isLafDark() ? lightenColor(colorBackground, 3) : darkenColor(colorBackground, 3);
        final var oddForegroundRowColor = isLafDark() ? lightenColor(colorForeground, 3) : darkenColor(colorForeground, 3);
        final var evenBackgroundRowColor = UIManager.getColor("Table.background");
        final var selectedBackgroundRowColor = UIManager.getColor("Table.selectionBackground");
        final var selectedForegroundRowColor = UIManager.getColor("Table.selectionForeground");
        final var selectedInativeBackgroundRowColor = UIManager.getColor("Table.selectionInactiveBackground");
        final var selectedInativeForegroundRowColor = UIManager.getColor("Table.selectionInactiveForeground");
        
        cell.setForeground(oddForegroundRowColor);
        
        if (row % 2 == 0) {
            cell.setBackground(evenBackgroundRowColor);
        } else {
            cell.setBackground(oddBackgroundRowColor);
        }
        
        if(row == table.getSelectedRow() && table.hasFocus()){
        	cell.setBackground(selectedBackgroundRowColor);
        	cell.setForeground(selectedForegroundRowColor);
        	
        } else if(row == table.getSelectedRow() && !table.hasFocus()){
        	cell.setBackground(selectedInativeBackgroundRowColor);
        	cell.setForeground(selectedInativeForegroundRowColor);
        }
        
        return cell;
    }
}
