package interfaces;

import enums.WaitTimes;
import utility.CustomElement;
import utility.ElementUtil;

import org.testng.Assert;

import org.jetbrains.annotations.NotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;

import java.util.*;
import java.util.stream.Collectors;

public interface ITable {

    /**
     * Inner class for table element(s)
     */
    class Table extends CustomElement {

        private By mainTableXpath,
                headersXpath,
                rowsXpath,
                cellsXpath;
        private Map<AbstractMap.SimpleEntry<String, Integer>, CustomElement> columns; // Map column header names & indexes to the column element

        public Table(By mainTableXpath, By headersXpath, By rowsXpath, By cellsXpath) {
            super(mainTableXpath);
            this.headersXpath = headersXpath;
            this.rowsXpath = rowsXpath;
            this.cellsXpath = cellsXpath;
            mapColumns();
        }

        private void mapColumns(){
            columns = new HashMap<>();
            List<CustomElement> cols = getColumns();
            for(int i = 0 ; i < cols.size() ; i++){
                CustomElement colElem = cols.get(i);
                columns.put(
                        new AbstractMap.SimpleEntry<>(colElem.getText().trim(), i++),
                        colElem);
            }
        }

        private List<CustomElement> getColumns() {
            // Retrieve all child elements
            try {
                return getChildren(headersXpath);
            } catch(TimeoutException columnsNotFound) {
                throw new NotFoundException(String.format(
                        "Could not find any column elements for the table %s using the configured headers xpath: %s",
                        getLocatorAsString(),
                        ElementUtil.getLocatorAsString(headersXpath)));
            }
        }

        public Integer getColumnIndex(String columnHeaderName) {
            return columns.keySet()
                    .stream()
                    .filter(entry -> entry.getKey().equals(columnHeaderName))
                    .map(AbstractMap.SimpleEntry::getValue)
                    .findAny()
                    .orElseThrow(() -> new NullPointerException(
                            String.format("No header found in the table matching '%s'. Available headers: %s",
                                    columnHeaderName,
                                    columns.keySet())));
        }

        private TableRow getRow(@NotNull List<List<String>> requiredRow) {
            List<String> requiredColumnHeaders = requiredRow.get(0),
                    requiredDataForRow = requiredRow.get(1);
            List<TableRow> allRows = getAllRows();
            rowLoop:
            for (TableRow row : allRows){
                for(int colIndex = 0 ; colIndex < requiredColumnHeaders.size() ; colIndex++) {
                    if(!row.getCellFromColumn(requiredColumnHeaders.get(colIndex)).getText().equals(requiredDataForRow.get(colIndex))) {
                        continue rowLoop;
                    }
                }
                return row;
            }
            throw new RuntimeException("Could not find the row with the following data: " + requiredDataForRow);
        }

        private @NotNull List<TableRow> getRows(@NotNull List<List<String>> requiredRows) {
            List<String> requiredColumnHeaders = requiredRows.get(0);
            List<TableRow> rows = new ArrayList<>();
            List<TableRow> allRows = getAllRows();
            requiredRowLoop:
            for (int requiredRowIndex = 1 ; requiredRowIndex < requiredRows.size() ; requiredRowIndex++) {
                rowLoop:
                for (TableRow row : allRows) {
                    for(int colIndex = 0 ; colIndex < requiredColumnHeaders.size() ; colIndex++) {
                        if(!row.getCellFromColumn(requiredColumnHeaders.get(colIndex))
                                .getText()
                                .equals(requiredRows.get(requiredRowIndex).get(colIndex))) {
                            continue rowLoop;
                        }
                    }
                    rows.add(row);
                    continue requiredRowLoop;
                }
                throw new RuntimeException("Could not find the row with the following data: " + requiredRows.get(requiredRowIndex));
            }
            return rows;
        }

        private List<TableRow> getAllRows() {
            return getChildren(rowsXpath).stream().map(TableRow::new).collect(Collectors.toList());
        }

        /**
         * Inner class for table row(s)
         */
        class TableRow extends CustomElement {
            private final String HAS_DATA_XPATH = "/child::*", // TODO - Refine xpath if needed
                               SELECT_ROW_XPATH = ""; // TODO - Refine xpath if needed

            public TableRow(@NotNull CustomElement rowElement) {
                super(rowElement.getElement(), rowElement.getLocator());
            }

            /**
             * Check to see whether the row has no data.
             * @return true if there is only one child element within the row
             */
            public boolean hasData() {
                return getChildren(ElementUtil.getXpath(HAS_DATA_XPATH)).size() > 0;
            }

            public CustomElement getCellFromColumn(String columnHeaderName) {
                return getChild(By.xpath(
                        ElementUtil.getLocatorAsString(cellsXpath) +
                                String.format("[%s]", getColumnIndex(columnHeaderName))),
                        WaitTimes.TABLE_GET_CELL_FROM_COLUMN_TIMEOUT);

            }

            public void select() {
                getChild(ElementUtil.getXpath(SELECT_ROW_XPATH)).getElement().click();
            }

            public boolean isSelected() { // TODO - Review if this isSelected operation works correctly
                return getElement().isSelected();
            }

        }

    }

    default void validateAllRowsAreSelected(String tableName) {
        if(tableName != null) {
            for (Table.TableRow row : getTableByName(tableName).getAllRows()) {
                Assert.assertTrue(row.isSelected());
            }
        } else {
            validateAllRowsAreSelected();
        }
    }

    default void validateAllRowsAreSelected() {
        for (Table.TableRow row : getTable().getAllRows()) {
            Assert.assertTrue(row.isSelected());
        }
    }

    default void validateRowsAreSelected(String tableName, List<List<String>> rows) {
        if(tableName != null) {
            for (Table.TableRow row : getTableByName(tableName).getRows(rows)) {
                Assert.assertTrue(row.isSelected());
            }
        } else {
            validateRowsAreSelected(rows);
        }
    }

    default void validateRowsAreSelected(List<List<String>> rows) {
        for (Table.TableRow row : getTable().getRows(rows)) {
            Assert.assertTrue(row.isSelected());
        }
    }

    default void validateVisibilityOfRows(String tableName, List<List<String>> rows, boolean isVisible) {
        if(tableName != null) {
            for (Table.TableRow row : getTableByName(tableName).getRows(rows)) {
                Assert.assertEquals(isVisible, ElementUtil.isElementVisible(row.getElement()));
            }
        } else {
            validateVisibilityOfRows(rows, isVisible);
        }
    }

    default void validateVisibilityOfRows(List<List<String>> rows, boolean isVisible) {
        for (Table.TableRow row : getTable().getRows(rows)) {
            Assert.assertEquals(isVisible, ElementUtil.isElementVisible(row.getElement()));
        }
    }

    default void selectRows(List<List<String>> rows) {
        for (Table.TableRow row : getTable().getRows(rows)) {
            row.select();
        }
    }

    default void selectRows(String tableName, List<List<String>> rows){
        if(tableName != null) {
            for (Table.TableRow row : getTableByName(tableName).getRows(rows)) {
                row.select();
            }
        } else {
            selectRows(rows);
        }
    }

    void selectRowByValueInColumn(String columnName, String valueInColumn);

    default void validateTableStructure(@NotNull List<List<String>> table) {
        int colSize = table.get(0).size();
        int rowSize = table.get(1).size();
        if(colSize < 1){ // Check table has at least one column of data
            throw new InputMismatchException("The datatable needs to specify at least one column in order to properly select the desired row");
        }
        if(rowSize != 2){ // Check that the table has header columns
            throw new InputMismatchException("The datatable must ONLY specify two rows; first row for table headers, and second row for their applicable values");
        }
    }

    Table getTableByName(String tableName);

    Table getTable();

}