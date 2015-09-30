package cz.honzovysachy.awt;

import java.awt.*;
import java.awt.event.*;

/**
 * Tlacitko MessageBoxu.
 * @author nemec
 *
 */
@SuppressWarnings("serial")
class MBButton extends Button {

  /**
   * ID tlacitka. Treba ID_OK.
   */
  public int id;

  /**
   * Konstruktor.
   * @param id ID tlacitka. Treba ID_OK.
   * @param popis Text na buttonu.
   */
  public MBButton(int id, String popis) {
    super(popis);
    this.id = id;
  }
}

/**
 * Trida reprezentujici jednoduchy MessageBox s nastavitelnymi tlacitky.
 * @author nemec
 *
 */
@SuppressWarnings("serial")
public class MessageBox extends Dialog implements ActionListener {

  int id = -1;
  MBButton[] buttony;

  MessageBox(String nadpis, String msg, MBButton[] buttony) {
    super(new Frame(""), nadpis, true);
    setLayout(new BorderLayout());
    add("Center", new Label(msg));
    addOKCancelPanel(buttony);
    createFrame();
    pack();
    setVisible(true);
  }

  void addOKCancelPanel(MBButton[] buttony) {
    Panel p = new Panel();
    p.setLayout(new FlowLayout());
    for (int i = 0; i < buttony.length; i++) {
      p.add(buttony[i]);
      buttony[i].addActionListener(this);
    }
    add("South", p);
  }

  void createFrame() {
    Dimension d = getToolkit().getScreenSize();
    setLocation(d.width / 3, d.height / 3);
  }

  public void actionPerformed(ActionEvent ae) {
    Object source = ae.getSource();
    if (!(source instanceof MBButton)) {
      return;
    }
    MBButton b = (MBButton) source;
    id = b.id;
    setVisible(false);
  }

  public static final int ID_YES = 0;
  public static final int ID_NO = 1;
  public static final int ID_OK = 2;
  public static final int ID_CANCEL = 3;

  public static final int MB_YES_NO = 0;
  public static final int MB_OK_CANCEL = 1;
  public static final int MB_OK = 2;
  public static final int MB_PROMENA = 3;

  public static int messageBox(String t1, String t2, MBButton[] buttony) {
    MessageBox message = new MessageBox(t1, t2, buttony);
    message.dispose();
    return message.id;
  }

  
  public static void messageBoxLater(final String t1, final String t2, final boolean konec) {
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            MessageBox.messageBox(t1, t2, MessageBox.MB_OK);
            if (konec) {
              System.exit(0);
            }
          }
        }
     );
  }
  
  public static int messageBox(String t1, String t2, int typ) {
    int delka;

    switch (typ) {
    case MB_YES_NO:
    case MB_OK_CANCEL:
      delka = 2;
      break;
    case MB_OK:
      delka = 1;
      break;
    case MB_PROMENA:
      delka = 4;
      break;
    default:
      throw new IllegalArgumentException("Neznámý typ MessageBoxu");
    }
    MBButton[] buttony = new MBButton[delka];
    switch (typ) {
    case MB_YES_NO:
      buttony[0] = new MBButton(ID_YES, "Ano");
      buttony[1] = new MBButton(ID_NO, "Ne");
      break;
    case MB_OK_CANCEL:
      buttony[0] = new MBButton(ID_OK, "Budiž");
      buttony[1] = new MBButton(ID_CANCEL, "Zrušit");
      break;
    case MB_OK:
      buttony[0] = new MBButton(ID_OK, "Budiž");
      break;
    case MB_PROMENA:
      buttony[0] = new MBButton(2, "Knight");
      buttony[1] = new MBButton(3, "Bishop");
      buttony[2] = new MBButton(4, "Rook");
      buttony[3] = new MBButton(5, "Queen");
      break;
      
    default:
      throw new IllegalArgumentException("Neznámý typ MessageBoxu");
    }
    return messageBox(t1, t2, buttony);
  }
}
