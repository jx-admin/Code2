package com.apk.infos;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;



public class IndexShowUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new IndexShowUI();
	}
	/** 选择文件夹按钮 */
	JButton JBtn_Select;
	/** 获取按钮 */
	JButton JBtn_Get;
	/** 显示文件夹路径 */
	JTextField JText_Path;
	/** 文件选择器 */
	JFileChooser jFileChooser;
	JScrollPane scrollPane;
	JTable table;
	public static String strResult;

	public IndexShowUI() {
		super("获取APK信息");
//		setBounds(500, 400, 0, 0);
		setSize(new Dimension(500, 400));
		
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);
		
		setResizable(false);
		JBtn_Select = new JButton("浏 览");
		JBtn_Get = new JButton("获 取");
		JPanel panel = new JPanel();
		JText_Path = new JTextField(25);
		JText_Path.setText(System.getProperty("user.home") + "\\桌面\\");
		panel.add(new JLabel("目标文件夹"));
		panel.add(JText_Path);
		panel.add(JBtn_Select);
		panel.add(JBtn_Get);
		jFileChooser = new JFileChooser();
		JPanel panelResult = new JPanel();
		panelResult.setOpaque(true);
		table=new JTable(new TableResult(""));
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 12));
        table.setRowHeight(28);
        scrollPane = new JScrollPane(table);
		panelResult.add(scrollPane);
		Container container = getContentPane();
		container.add(panel, BorderLayout.NORTH);
		container.add(panelResult, BorderLayout.CENTER);
		JBtn_Select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jFileChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设定只能选择到文件夹
				int state = jFileChooser.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
				if (state == 1) {
					return;// 撤销则返回
				} else {
					File f = jFileChooser.getSelectedFile();// f为选择到的目录
					JText_Path.setText(f.getAbsolutePath()+"\\");
				}
			}
		});
		JBtn_Get.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String strPath = JText_Path.getText();
				if (strPath.equals("")) {
					// 提示路径为空
					JOptionPane.showMessageDialog(IndexShowUI.this, "路径为空，请选择");
				} else {
					// 验证路径是否存在
					File file = new File(strPath);
					if (!file.exists()) {
						JOptionPane.showMessageDialog(IndexShowUI.this,
								"路径错误，重新选择");
					} else {
						// 执行
						if (!strPath.endsWith("\\")) {
							strPath=strPath+"\\";
							JText_Path.setText(strPath);
						}
						table.setModel(new TableResult(strPath));
						JOptionPane.showMessageDialog(IndexShowUI.this, strResult);
					}
				}
			}
		});
//		setSize(500, 200);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
