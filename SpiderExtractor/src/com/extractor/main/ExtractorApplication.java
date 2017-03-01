package com.extractor.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.extractor.utils.SelectableUtils;

import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class ExtractorApplication {

	protected Shell shell;
	private Text text_0;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ExtractorApplication window = new ExtractorApplication();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
		int sHeight = dem.height;
		int sWidth = dem.width;
		int fHeight = shell.getSize().y;
		int fWidth = shell.getSize().x;
		shell.setLocation((sWidth - fWidth) / 2, (sHeight - fHeight) / 2);
		shell.open();
		shell.layout();
		// shell.setImage(new Image(shell.getDisplay(), "resource/bg.jpg"));
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setSize(417, 485);
		shell.setText("爬虫工具");

		Label label_0 = new Label(shell, SWT.NONE);
		label_0.setBounds(37, 20, 52, 17);
		label_0.setText("链  接：");

		text_0 = new Text(shell, SWT.BORDER);
		text_0.setBounds(104, 17, 265, 23);

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(37, 58, 61, 17);
		label_1.setText("选择器1：");

		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(37, 99, 61, 17);
		label_2.setText("选择器2：");

		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(37, 141, 61, 17);
		label_3.setText("选择器3：");

		final Combo combo_1 = new Combo(shell, SWT.READ_ONLY);
		combo_1.setBounds(104, 55, 61, 25);
		combo_1.add("xpath");
		combo_1.add("css");
		combo_1.add("regex");
		combo_1.select(0);

		final Combo combo_2 = new Combo(shell, SWT.READ_ONLY);
		combo_2.setBounds(104, 96, 61, 25);
		combo_2.add("xpath");
		combo_2.add("css");
		combo_2.add("regex");
		combo_2.select(0);

		final Combo combo_3 = new Combo(shell, SWT.READ_ONLY);
		combo_3.setBounds(104, 138, 60, 25);
		combo_3.add("xpath");
		combo_3.add("css");
		combo_3.add("regex");
		combo_3.select(0);

		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(182, 55, 187, 23);

		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(182, 96, 187, 23);

		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(182, 138, 187, 23);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(37, 182, 61, 17);
		label_4.setText("配置选项：");

		final Button button3 = new Button(shell, SWT.CHECK);
		button3.setBounds(104, 182, 78, 17);
		button3.setText("结果为List");

		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(237, 182, 78, 17);
		label_5.setText("结果集大小：");

		final Label label_6 = new Label(shell, SWT.NONE);
		label_6.setBounds(321, 182, 34, 17);
		label_6.setText("0");

		Button button1 = new Button(shell, SWT.NONE);
		button1.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index1 = combo_1.getSelectionIndex();
				int index2 = combo_2.getSelectionIndex();
				int index3 = combo_3.getSelectionIndex();
				String url = text_0.getText();
				String selector1 = text_1.getText();
				String selector2 = text_2.getText();
				String selector3 = text_3.getText();

				boolean check1 = button3.getSelection();

				Map<String, String> selectorMap = new LinkedHashMap<>();
				if (StringUtils.isNotBlank(url)) {
					if (index1 >= 0 && StringUtils.isNotBlank(selector1)) {
						selectorMap.put(combo_1.getText(), selector1);
						if (index2 >= 0) {
							if (StringUtils.isNotBlank(selector2)) {
								selectorMap.put(combo_2.getText(), selector2);
							}
						}
						if (index3 >= 0) {
							if (StringUtils.isNotBlank(selector3)) {
								selectorMap.put(combo_3.getText(), selector3);
							}
						}
						if (selectorMap.size() > 0) {
							url = StringUtils.strip(url);
							try {
								Document document = Jsoup.connect(url).ignoreContentType(true).followRedirects(false)
										.timeout(1000 * 60)
										.userAgent(
												"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
										.header("Accept", "*/*")
										.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
										.header("Connection", "close").get();
								if (null != document && null != document.body()) {
									Html html = Html.create(document.html());
									Selectable selectable = SelectableUtils.select(html, selectorMap);
									if (null != selectable) {
										Object result = null;
										if (check1) {
											result = selectable.all();
										} else {
											result = selectable.get();
										}
										if (null != result) {
											StringBuilder builder = new StringBuilder();
											if (result instanceof List) {
												List list = (List) result;
												builder.append(list.toString());
												label_6.setText(String.valueOf(list.size()));
											} else {
												builder.append(result.toString());
												label_6.setText("1");
											}
											text_4.setText(builder.toString());
											text_4.setTopIndex(Integer.MAX_VALUE);
										}
									}
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ERROR);
								messageBox.setMessage("连接异常！请检查url是否正确！");
								messageBox.open();
							}
						}
					} else {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ERROR);
						messageBox.setMessage("选择器1不能为空！");
						messageBox.open();
					}
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ERROR);
					messageBox.setMessage("链接不能为空！");
					messageBox.open();
				}
			}
		});
		button1.setBounds(128, 416, 60, 27);
		button1.setText("选取");

		Button button2 = new Button(shell, SWT.NONE);
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo_1.select(0);
				combo_2.select(0);
				combo_3.select(0);
				text_0.setFocus();
				text_0.setText("");
				text_1.setText("");
				text_2.setText("");
				text_3.setText("");
				text_4.setText("");
				button3.setSelection(false);
				label_6.setText("0");
			}
		});
		button2.setBounds(224, 416, 61, 27);
		button2.setText("清空");

		text_4 = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		text_4.setBounds(37, 215, 332, 184);
	}
}
