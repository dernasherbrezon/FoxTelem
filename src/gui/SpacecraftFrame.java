package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import common.Config;
import common.Log;
import common.Spacecraft;

public class SpacecraftFrame extends JDialog implements ItemListener, ActionListener, FocusListener {

	private final JPanel contentPanel = new JPanel();
	JTextField telemetryDownlinkFreqkHz;
	JTextField minFreqBoundkHz;
	JTextField maxFreqBoundkHz;
	JTextField rssiLookUpTableFileName;
	JTextField ihuTempLookUpTableFileName;
	JTextField ihuVBattLookUpTableFileName;
	JTextField BATTERY_CURRENT_ZERO;
	
	JCheckBox useIHUVBatt;
	JCheckBox track;

	JButton btnCancel;
	JButton btnSave;
	
	Spacecraft sat;

	int headerSize = 12;
	
	/**
	 * Create the dialog.
	 */
	public SpacecraftFrame(Spacecraft sat, JFrame owner, boolean modal) {
		super(owner, modal);
		setTitle("Spacecraft paramaters");
		this.sat = sat;
		setBounds(100, 100, 550, 550);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		contentPanel.setLayout(new BorderLayout(0, 0));
		addFields();
		
		addButtons();
		
	}
	
	private void addFields() {
		JPanel titlePanel = new JPanel();
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		TitledBorder heading0 = title("Identification");
		titlePanel.setBorder(heading0);

		JLabel lName = new JLabel("Name: " + sat.name);
		titlePanel.add(lName);
		JLabel lId = new JLabel("     ID: " + sat.foxId);
		titlePanel.add(lId);
		
		// Left Column - Fixed Params that can not be changed
		JPanel leftPanel = new JPanel();
		contentPanel.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		JPanel leftFixedPanel = new JPanel();
		leftFixedPanel.setLayout(new BoxLayout(leftFixedPanel, BoxLayout.Y_AXIS));
		leftPanel.add(leftFixedPanel);
		
		TitledBorder heading = title("Fixed Paramaters");
		leftFixedPanel.setBorder(heading);
		
		JLabel lModel = new JLabel("Model: " + Spacecraft.modelNames[sat.model]);
		leftFixedPanel.add(lModel);
		JLabel lIhusn = new JLabel("IHU S/N: " + sat.IHU_SN);
		leftFixedPanel.add(lIhusn);
		
		JLabel lExp[] = new JLabel[4];
		for (int i=0; i<4; i++) {
			lExp[i] = new JLabel("Experiment "+(i+1)+": " + Spacecraft.expNames[sat.experiments[i]]);
			leftFixedPanel.add(lExp[i]);
		}


		JPanel t0Panel = new JPanel();
		leftPanel.add(t0Panel);
		
		TitledBorder headingT0 = title("Time Zero");
		t0Panel.setBorder(headingT0);
		t0Panel.setLayout(new BoxLayout(t0Panel, BoxLayout.Y_AXIS));
		
		if (sat.hasTimeZero(0)) {
			JLabel T0 = new JLabel("Reset 0: " + sat.getUtcDateforReset(0, 0) + " " + sat.getUtcTimeforReset(0, 0));
			t0Panel.add(T0);
		} else {
			JLabel T1 = new JLabel("Reset 0: Time Origin missing");
			t0Panel.add(T1);
		}
		if (sat.hasTimeZero(1)) {
			JLabel T1 = new JLabel("Reset 1: " + sat.getUtcDateforReset(1, 0) + " " + sat.getUtcTimeforReset(1, 0));
			t0Panel.add(T1);
		} else {
			JLabel T1 = new JLabel("Reset 1: Time Origin missing");
			t0Panel.add(T1);
		}
		leftPanel.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));
		

		
		// Right Column - Things the user can change - e.g. Layout Files, Freq, Tracking etc
		JPanel rightPanel = new JPanel();
		contentPanel.add(rightPanel, BorderLayout.CENTER);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		JPanel rightPanel1 = new JPanel();
		rightPanel.add(rightPanel1);
		rightPanel1.setLayout(new BoxLayout(rightPanel1, BoxLayout.Y_AXIS));
		
		TitledBorder heading2 = title("Frequency and Tracking");
		rightPanel1.setBorder(heading2);
				
		telemetryDownlinkFreqkHz = addSettingsRow(rightPanel1, 15, "Downlink Freq (kHz)", 
				"The nominal downlink frequency of the spacecraft", ""+sat.telemetryDownlinkFreqkHz);
		minFreqBoundkHz = addSettingsRow(rightPanel1, 15, "Lower Freq Bound (kHz)", 
				"The lower frequency boundry when we are searching for the spacecraft signal", ""+sat.minFreqBoundkHz);
		maxFreqBoundkHz = addSettingsRow(rightPanel1, 15, "Upper Freq Bound (kHz)", 
				"The upper frequency boundry when we are searching for the spacecraft signal", ""+sat.maxFreqBoundkHz);
		track = addCheckBoxRow("Track when Find Signal Enabled", "When Find Signal is enabled include this satellite in the search", sat.track, rightPanel1 );
		rightPanel1.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));

		JPanel rightPanel2 = new JPanel();
		rightPanel.add(rightPanel2);
		rightPanel2.setLayout(new BoxLayout(rightPanel2, BoxLayout.Y_AXIS));
		
		TitledBorder heading3 = title("Calibration");
		rightPanel2.setBorder(heading3);

		BATTERY_CURRENT_ZERO = addSettingsRow(rightPanel2, 25, "Battery Current Zero", 
				"The calibration paramater for zero battery current", ""+sat.BATTERY_CURRENT_ZERO);

		rssiLookUpTableFileName = addSettingsRow(rightPanel2, 25, "RSSI Lookup Table", 
				"The file containing the lookup table for Received Signal Strength", ""+sat.rssiLookUpTableFileName);
		ihuTempLookUpTableFileName = addSettingsRow(rightPanel2, 25, "IHU Temp Lookup Table", 
				"The file containing the lookup table for the IHU Temperature", ""+sat.ihuTempLookUpTableFileName);
		ihuVBattLookUpTableFileName = addSettingsRow(rightPanel2, 25, "VBatt Lookup Table", 
				"The file containing the lookup table for the Battery Voltage", ""+sat.ihuVBattLookUpTableFileName);
	
		useIHUVBatt = addCheckBoxRow("Use Bus Voltage as VBatt", "Read the Bus Voltage from the IHU rather than the Battery "
				+ "Voltage from the battery card using I2C", sat.useIHUVBatt, rightPanel2 );
		
		rightPanel2.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));

		
		// Bottom panel for description
		JPanel footerPanel = new JPanel();
		TitledBorder heading9 = title("Description");
		footerPanel.setBorder(heading9);

		JTextArea taDesc = new JTextArea(2, 45);
		taDesc.setText(sat.description);
		taDesc.setLineWrap(true);
		taDesc.setWrapStyleWord(true);
		taDesc.setEditable(false);
		taDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
		footerPanel.add(taDesc);
		contentPanel.add(footerPanel, BorderLayout.SOUTH);
	}
	
	private TitledBorder title(String s) {
		TitledBorder title = new TitledBorder(null, s, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		title.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
		return title;
	}
	
	private void addButtons() {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnSave = new JButton("Save");
		btnSave.setActionCommand("Save");
		buttonPane.add(btnSave);
		btnSave.addActionListener(this);
		getRootPane().setDefaultButton(btnSave);


		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
		btnCancel.addActionListener(this);

	}

	private JCheckBox addCheckBoxRow(String name, String tip, boolean value, JPanel parent) {
		JPanel box = new JPanel();
		box.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JCheckBox checkBox = new JCheckBox(name);
		checkBox.setEnabled(true);
		checkBox.addItemListener(this);
		checkBox.setToolTipText(tip);
		box.add(checkBox);
		parent.add(box);
		if (value) checkBox.setSelected(true); else checkBox.setSelected(false);
		return checkBox;
	}


	private JTextField addSettingsRow(JPanel column, int length, String name, String tip, String value) {
		JPanel panel = new JPanel();
		column.add(panel);
		panel.setLayout(new GridLayout(1,2,5,5));
		JLabel lblDisplayModuleFont = new JLabel(name);
		lblDisplayModuleFont.setToolTipText(tip);
		panel.add(lblDisplayModuleFont);
		JTextField textField = new JTextField(value);
		panel.add(textField);
		textField.setColumns(length);
		textField.addActionListener(this);
		textField.addFocusListener(this);

		column.add(new Box.Filler(new Dimension(10,5), new Dimension(10,5), new Dimension(10,5)));

		return textField;
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean refreshTabs = false;
		if (e.getSource() == btnCancel) {
			this.dispose();
		}
		if (e.getSource() == btnSave) {
			try {
				try {
					sat.telemetryDownlinkFreqkHz = Integer.parseInt(telemetryDownlinkFreqkHz.getText());
					sat.minFreqBoundkHz = Integer.parseInt(minFreqBoundkHz.getText());
					sat.maxFreqBoundkHz = Integer.parseInt(maxFreqBoundkHz.getText());
				} catch (NumberFormatException ex) {
					throw new Exception("The Frequency fields must contain a valid number");
				}
				if (sat.rssiLookUpTableFileName != rssiLookUpTableFileName.getText()) {
					sat.rssiLookUpTableFileName = rssiLookUpTableFileName.getText();
					refreshTabs = true;
				}
				if (sat.ihuTempLookUpTableFileName != ihuTempLookUpTableFileName.getText()) {
					sat.ihuTempLookUpTableFileName = ihuTempLookUpTableFileName.getText();
					refreshTabs = true;
				}
				if (sat.ihuVBattLookUpTableFileName != ihuVBattLookUpTableFileName.getText()) {
					sat.ihuVBattLookUpTableFileName = ihuVBattLookUpTableFileName.getText();
					refreshTabs = true;
				}
				
				if (sat.BATTERY_CURRENT_ZERO != Double.parseDouble(BATTERY_CURRENT_ZERO.getText())) {
					sat.BATTERY_CURRENT_ZERO = Double.parseDouble(BATTERY_CURRENT_ZERO.getText());
					refreshTabs=true;
				}

				if (sat.useIHUVBatt != useIHUVBatt.isSelected()) {
					sat.useIHUVBatt = useIHUVBatt.isSelected();
					refreshTabs = true;
				}
				sat.track = track.isSelected();

				if (refreshTabs)
					MainWindow.refreshTabs(false);
				sat.save();
				this.dispose();
			} catch (Exception Ex) {
				Log.errorDialog("Invalid Paramaters", Ex.getMessage());
			}
		}

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
