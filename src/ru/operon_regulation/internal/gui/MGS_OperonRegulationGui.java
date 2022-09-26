package ru.operon_regulation.internal.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ru.operon_regulation.internal.interfaces.*;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSstringHelper;

public class MGS_OperonRegulationGui extends JFrame
{
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 300;

	TableModel dataSourceModel;

	private String outModelPath = "D:/FEDOR/ICiG/MGS/MGSgenerator/modules/GeneRegulationEquations/SRC/TEST/outModel.txt";
	JButton makeModelButton = new JButton();
	JTextField pathToFile = new JTextField(outModelPath);

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public MGS_OperonRegulationGui(String arg0, IMGS_OperonBase base)
			throws java.awt.HeadlessException
	{

		super("Operon selection module");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		dataSourceModel = new OperonTableModel(base);

		JTable table = new JTable(dataSourceModel);
		add(new JScrollPane(table));

		table.repaint();
		table.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				if (e.getButton() == e.BUTTON3)
				{
					System.err.println("doit");
					doSaveModel();
				}
			}
		});
	}

	public void doSaveModel()
	{
		String outText = "";
		int sz = dataSourceModel.getRowCount();
		int chInd = dataSourceModel.getColumnCount() - 1;
		for (int i = 0; i < sz; i++)
		{
			if ((Boolean) (dataSourceModel.getValueAt(i, chInd)) == true)
			{
				outText += dataSourceModel.getValueAt(i, 0) + MGSstringHelper.NEXT_LINE;
			}
		}
		MGSfilesHelper.writeStringInFile(outText, outModelPath);
	}

	class OperonTableModel extends AbstractTableModel
	{
		private final int lastColumn = 3;
		/**
		 * index of column where check flag
		 */
		private final int checkColumnIndex = 3;
		private int rowCount = 0;
		private final String[] columnNames = { "Operon", "TF size", "check" };
		private Object[][] sourceData;

		public OperonTableModel(IMGS_OperonBase base)
		{
			makeInit(base);
		}

		private void makeInit(IMGS_OperonBase base)
		{
			List<IMGS_Operon> operons = base.getOperons();
			int rowCount = operons.size();
			sourceData = new Object[rowCount][lastColumn];
			int rowCounter = 0;
			for (IMGS_Operon oper : operons)
			{

				sourceData[rowCounter][0] = oper.getName();
				sourceData[rowCounter][1] = oper.getTFactors().length;
				sourceData[rowCounter][2] = new Boolean(false);
				rowCounter++;
			}
		}

		@Override
		public int getColumnCount()
		{
			return lastColumn;
		}

		@Override
		public int getRowCount()
		{
			//return rowCount;
			return sourceData.length;
		}

		@Override
		public Object getValueAt(int r, int c)
		{
			return sourceData[r][c];
		}

		@Override
		public String getColumnName(int column)
		{
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return (columnIndex == (checkColumnIndex - 1));
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			if (columnIndex == (checkColumnIndex - 1))
			{
				sourceData[rowIndex][columnIndex] = value;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			return sourceData[0][columnIndex].getClass();
		}
	}
}
