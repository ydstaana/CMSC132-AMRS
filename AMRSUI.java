import javax.swing.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class AMRSUI{
	JFrame frame = new JFrame("AMRS");
	JPanel mainPanel = new JPanel();
	JPanel fetchPanel = new JPanel();
	JPanel innerFetchPanel = new JPanel();
	JPanel decodePanel = new JPanel();
	JPanel executePanel = new JPanel();
	JPanel memoryPanel = new JPanel();
	JPanel writeBackPanel = new JPanel();
public static ArrayList<String> instructionSet = new ArrayList<String>();
	public AMRSUI(){
		//AMRS amrs = new AMRS();
		BufferedReader br = null;
		FileReader fr = null;
		
		//mainPanel.setPreferredSize(new Dimension(300, 500));
		
		try{
			fr = new FileReader("inputfile.txt");
			br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				instructionSet.add(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		JTable memoryTable = new JTable(toTableModel(instructionSet));
		JScrollPane memoryTablePane = new JScrollPane(memoryTable);
		memoryTablePane.setPreferredSize(new Dimension(100, 300));

		innerFetchPanel.setLayout(new BoxLayout(innerFetchPanel, BoxLayout.Y_AXIS));
		innerFetchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		innerFetchPanel.add(new JLabel("PC"));
		JTextField pcTextField = new JTextField(5);
		pcTextField.setEditable(false);
		innerFetchPanel.add(pcTextField);

		innerFetchPanel.add(new JLabel("MAR"));
		JTextField marTextField = new JTextField(5);
		marTextField.setEditable(false);
		innerFetchPanel.add(marTextField);

		innerFetchPanel.add(new JLabel("MBR"));
		JTextField mbrTextField = new JTextField(5);
		mbrTextField.setEditable(false);
		innerFetchPanel.add(mbrTextField);

		fetchPanel.add(memoryTablePane);
		fetchPanel.add(innerFetchPanel);

		mainPanel.add(fetchPanel);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String args[]){
		new AMRSUI();
	}
	public static TableModel toTableModel(ArrayList<String> instructionSet) {							//this table model will be used for the output table					
	    DefaultTableModel model = new DefaultTableModel(new Object[]{"Instructions"}, 0){
	    	@Override
		    public boolean isCellEditable(int row, int column) {				
		       return false;
		    }
	    };
	    /*for (Iterator it = map.entrySet().iterator(); it.hasNext();){			
	        Map.Entry entry = (Map.Entry)it.next();
	        model.addRow(new Object[] { entry.getKey(), entry.getValue() });
	    }*/

	    for(int i = 0; i < instructionSet.size(); i++){
			System.out.println(instructionSet.get(i));
			model.addRow(new Object[]{instructionSet.get(i)});
		}

	    return model;
	}
}