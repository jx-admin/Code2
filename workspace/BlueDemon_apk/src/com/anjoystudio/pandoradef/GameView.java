package com.anjoystudio.pandoradef;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import org.apache.commons.codec.binary.Base64;

public class GameView extends SurfaceView
  implements Runnable, SurfaceHolder.Callback
{
  public static final int ABOUT = 3;
  public static final int CHOICE_MAP = 8;
  public static final int CHOICE_MODE = 9;
  public static final String FILE_NAME = "pandoraDefence";
  public static final int GAME = 7;
  public static final int HELP = 4;
  public static final int HIGHT_SCORE = 5;
  public static final int LOGO = 0;
  public static final int MAIN_MENU = 1;
  public static int[][] NPC_NULL_PATH;
  public static final int OPTIONS = 12;
  public static final int QUIT_OPTIONS = 11;
  public static final int SURE_TO_START_NEW_GAME = 2;
  public static Bitmap bm_all_b;
  public static Bitmap bm_choice_mode;
  public static Bitmap bm_exit;
  public static Bitmap bm_faster;
  public static Bitmap bm_fasterer;
  public static Bitmap bm_heartcount;
  public static Bitmap bm_main;
  public static Bitmap bm_main_menu;
  public static Bitmap bm_map;
  public static Bitmap bm_moneycount;
  public static Bitmap[][] bm_npc;
  public static Bitmap[][] bm_npc_green;
  public static Bitmap[][] bm_npc_red;
  public static Bitmap bm_options;
  public static Bitmap bm_pause;
  public static Bitmap bm_resume;
  public static Bitmap bm_round;
  public static Bitmap bm_roundcount;
  public static Bitmap bm_setting;
  public static Bitmap bm_z;
  public static Bitmap bmpBuildFeeNum;
  static Bitmap bmpBulletBomb;
  static Bitmap bmpBulletBoom;
  static Bitmap bmpBulletFire;
  static Bitmap bmpBulletSlowdown;
  static Bitmap bmpHeart;
  static Bitmap bmpIconGooTower;
  static Bitmap bmpIconMoney;
  static Bitmap bmpIconNoMoney;
  static Bitmap bmpMoney;
  static Bitmap bmpMoveTower;
  static Bitmap[] bmpTower;
  public static Bitmap bmp_mai;
  public static Bitmap bmp_update;
  public static int choice_map;
  public static int difficulty;
  public static int fresh_time;
  public static Thread game_thread;
  public static int heart;
  public static int[] in_0;
  public static int[] in_1;
  public static boolean isReading;
  public static boolean is_faster;
  public static boolean is_first_run = true;
  public static boolean is_mapDust_open;
  public static boolean is_mapDust_unlimit_open;
  public static boolean is_mapForest_open;
  public static boolean is_mapForest_unlimit_open;
  public static boolean is_mapHill_open;
  public static boolean is_mapHill_unlimit_open;
  public static boolean is_mapICC_unlimit_open;
  public static boolean is_playmusic;
  public static boolean is_shake;
  static int kind;
  public static int last_state;
  public static byte[][] map;
  public static int mode;
  public static int money;
  public static MediaPlayer mp_boomb;
  public static MediaPlayer mp_fire;
  public static MediaPlayer mp_flood;
  public static MediaPlayer mp_gun;
  public static MediaPlayer mp_music;
  public static MediaPlayer mp_roket;
  public static MediaPlayer mp_slowdown;
  static boolean next_wave;
  public static Npc[] npc;
  public static int off_x;
  public static int off_y;
  public static int[] out_0;
  public static int[] out_1;
  public static Random random;
  public static int refresh_time;
  public static boolean sale_to_regetpath;
  public static int score;
  public static int scr_h;
  public static int scr_w;
  static int startX;
  static int startY;
  public static int state = 0;
  public static Tool tool;
  static Tower tower;
  static int towerCount;
  static ArrayList<Tower> towers;
  public static Vibrator vibrator;
  public static int wave;
  public static boolean will_music;
  public static boolean will_not_play_music;
  public final int RATE;
  public int about_select;
  public int about_time;
  public Action act_menu;
  int beforeMoveX;
  int beforeMoveY;
  public Bitmap bm_about;
  public Bitmap bm_choice_map;
  public Bitmap bm_help;
  public Bitmap bm_help_image;
  public Bitmap bm_hight_score;
  public Bitmap[] bm_logos;
  Bitmap bm_menu_z;
  public Bitmap bm_sure;
  public boolean canBuild;
  int canBuildNum;
  public int choiceMap_selected;
  public int choiceMap_time;
  public int choiceMode_selected;
  public int choiceMode_time;
  int con;
  public Dialog dia_options;
  int dis_x;
  int dis_y;
  public boolean game_pause;
  GameIo gi;
  public int help_index;
  public int help_selected;
  public int help_time;
  public int high_selected;
  public int high_time;
  public int highscore_choice_map;
  public HightScoreMemory[] hiscoMemory;
  boolean isOutOfSelectArea;
  boolean isReallyMove;
  boolean isSelect;
  boolean isShowUpdate;
  public boolean is_exit;
  public boolean is_game_start;
  public CharSequence[] items;
  public int logo_time;
  public int mainMenu_selected;
  public int mainMenu_time;
  boolean mainmenu_canSelect;
  int map_x;
  int map_y;
  boolean menu_move_left;
  boolean menu_move_right;
  int menuz_x;
  int menuz_y;
  public int more_selected;
  public int more_time;
  int move_tower_x;
  int move_tower_y;
  public int[][] npc_actionFile;
  public int options_selected;
  public int options_time;
  public Paint p = new Paint();
  public boolean pause;
  int[] pre;
  public int prs_x;
  public int prs_y;
  public int quit_selected;
  public int quit_time;
  Resources res;
  int selectedTowerType;
  public int sureNewGame_selected;
  public int sureNewGame_time;
  public SurfaceHolder surface_holder;
  public boolean thread_run;
  public Toast toast;
  int touchEvent;
  int touchX;
  int touchY;
  public boolean will_check_path;

  static
  {
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[0] = 6;
    arrayOfInt1[1] = 2;
    bm_npc = (Bitmap[][])Array.newInstance(Bitmap.class, arrayOfInt1);
    heart = 20;
    npc = new Npc[20];
    int[] arrayOfInt2 = new int[2];
    arrayOfInt2[0] = 6;
    arrayOfInt2[1] = 2;
    bm_npc_red = (Bitmap[][])Array.newInstance(Bitmap.class, arrayOfInt2);
    int[] arrayOfInt3 = new int[2];
    arrayOfInt3[0] = 6;
    arrayOfInt3[1] = 2;
    bm_npc_green = (Bitmap[][])Array.newInstance(Bitmap.class, arrayOfInt3);
    in_0 = new int[2];
    in_1 = new int[2];
    out_0 = new int[2];
    out_1 = new int[2];
    is_playmusic = true;
    is_shake = true;
    difficulty = 0;
    mode = 0;
    sale_to_regetpath = false;
    int[][] arrayOfInt = new int[2][];
    arrayOfInt[0] = new int[2];
    arrayOfInt[1] = new int[2];
    NPC_NULL_PATH = arrayOfInt;
    random = new Random();
    will_music = false;
    will_not_play_music = false;
    isReading = false;
  }

  public GameView(Context paramContext)
  {
    super(paramContext);
    CharSequence[] arrayOfCharSequence = new CharSequence[2];
    arrayOfCharSequence[0] = "开启声音";
    arrayOfCharSequence[1] = "开启震动";
    this.items = arrayOfCharSequence;
    this.bm_logos = new Bitmap[2];
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = 6;
    arrayOfInt[1] = 2;
    this.npc_actionFile = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt));
    this.pre = new int[2];
    this.will_check_path = true;
    this.mainMenu_time = -1;
    this.sureNewGame_time = -1;
    this.sureNewGame_selected = 0;
    this.choiceMap_time = -1;
    this.choiceMap_selected = 0;
    this.choiceMode_time = -1;
    this.choiceMode_selected = 0;
    this.about_time = -1;
    this.help_time = -1;
    this.high_time = -1;
    this.more_time = -1;
    this.options_time = -1;
    this.quit_time = -1;
    this.gi = null;
    this.isShowUpdate = true;
    this.menu_move_right = false;
    this.menu_move_left = false;
    this.mainmenu_canSelect = false;
    this.RATE = 41;
    this.p.setAntiAlias(true);
    tool = new Tool(paramContext);
    this.res = paramContext.getResources();
    vibrator = (Vibrator)MainActivity.mainActivity.getSystemService("vibrator");
    bm_z = tool.creat_bitmap(2130837562);
    this.toast = Toast.makeText(MainActivity.mainActivity, "", 0);
    loadmenu();
    this.thread_run = true;
    this.surface_holder = getHolder();
    this.surface_holder.addCallback(this);
    setFocusable(true);
    is_first_run = true;
    scr_w = MainActivity.scr_w;
    scr_h = MainActivity.scr_h;
    if (state != 7)
    {
      initLogoImg();
      initTotalImg();
      this.menu_move_right = false;
      this.menu_move_left = false;
      this.menuz_x = 190;
      this.menuz_y = 265;
    }
    game_thread = new Thread(this);
  }

  private void drawAbout(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.bm_about == null)
      initAboutImg();
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_about, scr_w >> 1, scr_h >> 1, 3);
    if (this.about_time >= 0)
    {
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.about_select)
      {
      default:
      case 0:
      }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - this.bm_about.getWidth() >> 1), 28 + (scr_h - this.bm_about.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void drawChoiceMap(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.bm_choice_map == null)
      initChoiceMapImg();
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_choice_map, scr_w >> 1, scr_h >> 1, 3);
    if (this.choiceMap_time >= 0)
    {
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.choiceMap_selected)
      {
      default:
      case 4:
      }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - this.bm_choice_map.getWidth() >> 1), 28 + (scr_h - this.bm_choice_map.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void drawChoiceMode(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (bm_choice_mode == null)
      initChoiceMode();
    Tool.draw_bitmap(paramCanvas, this.p, bm_choice_mode, scr_w >> 1, scr_h >> 1, 3);
    this.p.setStyle(Paint.Style.STROKE);
    this.p.setColor(-256);
    if (bm_z == null)
      bm_z = tool.creat_bitmap(2130837562);
    switch (choice_map)
    {
    default:
      switch (difficulty)
      {
      default:
        switch (mode)
        {
        default:
          label160: label188: label216: if (this.choiceMode_time < 0)
            break label268;
          if (bm_all_b == null)
            bm_all_b = tool.creat_bitmap(2130837505);
          switch (this.choiceMode_selected)
          {
          default:
          case 0:
          case 1:
          }
        case 0:
        case 1:
        case 2:
        }
      case 0:
      case 1:
      case 2:
      }
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      label268: return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 110 + (scr_w - bm_choice_mode.getWidth() >> 1), 72 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 44, 110, 19, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 76 + (scr_w - bm_choice_mode.getWidth() >> 1), 119 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 238, bm_z.getWidth(), 80, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 298 + (scr_w - bm_choice_mode.getWidth() >> 1), 69 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 470, 108, 72, 0);
      break label160:
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 110 + (scr_w - bm_choice_mode.getWidth() >> 1), 72 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 64, 110, 21, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 76 + (scr_w - bm_choice_mode.getWidth() >> 1), 119 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 320, bm_z.getWidth(), 80, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 298 + (scr_w - bm_choice_mode.getWidth() >> 1), 69 + (scr_h - bm_choice_mode.getHeight() >> 1), 108, 470, 108, 72, 0);
      break label160:
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 110 + (scr_w - bm_choice_mode.getWidth() >> 1), 72 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 24, 110, 19, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 76 + (scr_w - bm_choice_mode.getWidth() >> 1), 119 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 160, bm_z.getWidth(), 75, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 298 + (scr_w - bm_choice_mode.getWidth() >> 1), 69 + (scr_h - bm_choice_mode.getHeight() >> 1), 108, 542, 108, 72, 0);
      break label160:
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 110 + (scr_w - bm_choice_mode.getWidth() >> 1), 72 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 0, 110, 24, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 76 + (scr_w - bm_choice_mode.getWidth() >> 1), 119 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 85, bm_z.getWidth(), 75, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_z, 298 + (scr_w - bm_choice_mode.getWidth() >> 1), 69 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 542, 108, 72, 0);
      break label160:
      paramCanvas.drawRect(72 + (scr_w - bm_choice_mode.getWidth() >> 1), 227 + (scr_h - bm_choice_mode.getHeight() >> 1), 138 + (scr_w - bm_choice_mode.getWidth() >> 1), 247 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label188:
      paramCanvas.drawRect(138 + (scr_w - bm_choice_mode.getWidth() >> 1), 227 + (scr_h - bm_choice_mode.getHeight() >> 1), 204 + (scr_w - bm_choice_mode.getWidth() >> 1), 247 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label188:
      paramCanvas.drawRect(204 + (scr_w - bm_choice_mode.getWidth() >> 1), 227 + (scr_h - bm_choice_mode.getHeight() >> 1), 270 + (scr_w - bm_choice_mode.getWidth() >> 1), 247 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label188:
      paramCanvas.drawRect(305 + (scr_w - bm_choice_mode.getWidth() >> 1), 173 + (scr_h - bm_choice_mode.getHeight() >> 1), 400 + (scr_w - bm_choice_mode.getWidth() >> 1), 195 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label216:
      paramCanvas.drawRect(305 + (scr_w - bm_choice_mode.getWidth() >> 1), 195 + (scr_h - bm_choice_mode.getHeight() >> 1), 400 + (scr_w - bm_choice_mode.getWidth() >> 1), 220 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label216:
      paramCanvas.drawRect(305 + (scr_w - bm_choice_mode.getWidth() >> 1), 220 + (scr_h - bm_choice_mode.getHeight() >> 1), 400 + (scr_w - bm_choice_mode.getWidth() >> 1), 243 + (scr_h - bm_choice_mode.getHeight() >> 1), this.p);
      break label216:
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 108 + (scr_w - bm_choice_mode.getWidth() >> 1), 260 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 108, 120, 33, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - bm_choice_mode.getWidth() >> 1), 29 + (scr_h - bm_choice_mode.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void drawGame(Canvas paramCanvas)
  {
    int k;
    label87: int j;
    for (int i = 0; ; ++i)
    {
      if (i >= Data.MAP_PAINT[choice_map].length)
      {
        if (this.isOutOfSelectArea)
          drawBuildGird(paramCanvas);
        drawSpriteAfterSort(paramCanvas);
        if (this.isOutOfSelectArea)
        {
          tower.drawCircle(paramCanvas, this.move_tower_x, this.move_tower_y, this.selectedTowerType, this.p);
          tower.drawMoveTower(paramCanvas, this.move_tower_x, this.move_tower_y, this.selectedTowerType, this.p);
        }
        k = 0;
        if (k < towers.size())
          break label300;
        if (!this.isOutOfSelectArea)
          drawUI(paramCanvas);
        return;
      }
      j = 0;
      label113: if (j < Data.MAP_PAINT[choice_map][i].length)
        break;
    }
    if ((72 + (j * 72 + off_x) < 0) || (j * 72 + off_x > scr_w) || (72 + (i * 72 + off_y) < 0) || (i * 72 + off_y > scr_h));
    while (true)
    {
      ++j;
      break label113:
      paramCanvas.save();
      Tool.set_clip(j * 72 + off_x, i * 72 + off_y, 72, 72, paramCanvas);
      paramCanvas.drawBitmap(bm_map, j * 72 - 72 * ((Data.MAP_PAINT[choice_map][i][j] - 1) % 9) + off_x, i * 72 - 72 * ((Data.MAP_PAINT[choice_map][i][j] - 1) / 9) + off_y, this.p);
      paramCanvas.restore();
    }
    label300: if (towers.get(k) == null);
    while (true)
    {
      ++k;
      break label87:
      Tower localTower = (Tower)towers.get(k);
      if (localTower.isPressTower())
        localTower.drawUpdateCircle(paramCanvas, this.p, bmp_mai, bmp_update);
      if ((48 + (localTower.bullet.x + off_x) < 0) || (localTower.bullet.x + off_x - 48 > scr_w) || (48 + (localTower.bullet.y + off_y) < 0) || (localTower.bullet.y + off_y - 48 > scr_h))
        continue;
      localTower.bullet.paint(paramCanvas, this.p);
    }
  }

  private void drawHelp(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.bm_help == null)
      initHelpImg();
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_help, scr_w >> 1, scr_h >> 1, 3);
    if (bm_all_b == null)
      bm_all_b = tool.creat_bitmap(2130837505);
    switch (this.help_index)
    {
    default:
      label140: if (this.help_time < 0)
        break label176;
      switch (this.help_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      label176: return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, this.bm_help_image, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 0, 285, 160, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 150, 340, 31, 0);
      break label140:
      Tool.draw_bitmap_clip(paramCanvas, this.p, this.bm_help_image, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 160, 285, 160, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 202, 253, 31, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 85 + (scr_w - this.bm_help.getWidth() >> 1), 115 + (scr_h - this.bm_help.getHeight() >> 1), 253, 202, 100, 31, 0);
      break label140:
      Tool.draw_bitmap_clip(paramCanvas, this.p, this.bm_help_image, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 320, 285, 160, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 85 + (scr_w - this.bm_help.getWidth() >> 1), 85 + (scr_h - this.bm_help.getHeight() >> 1), 0, 238, 340, 31, 0);
      break label140:
      if (this.help_index <= 0)
        continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 38 + (scr_w - this.bm_help.getWidth() >> 1), 175 + (scr_h - this.bm_help.getHeight() >> 1), 4, 50, 50, 23, 0);
      continue;
      if (this.help_index >= 2)
        continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 374 + (scr_w - this.bm_help.getWidth() >> 1), 174 + (scr_h - this.bm_help.getHeight() >> 1), 0, 77, 70, 25, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - this.bm_help.getWidth() >> 1), 29 + (scr_h - this.bm_help.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void drawHighScore(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.bm_hight_score == null)
      initHightScoreImg();
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_hight_score, scr_w >> 1, scr_h >> 1, 3);
    this.p.setColor(-256);
    this.p.setStyle(Paint.Style.STROKE);
    label148: int i;
    switch (this.highscore_choice_map)
    {
    default:
      i = 0;
      label150: if (i < this.hiscoMemory.length)
        break label278;
      if (this.high_time < 0)
        break label208;
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.high_selected)
      {
      default:
      case 0:
      }
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      label208: return;
      drawRect(paramCanvas, 37, 65, 96, 110);
      break label148:
      drawRect(paramCanvas, 37, 114, 96, 156);
      break label148:
      drawRect(paramCanvas, 37, 162, 96, 206);
      break label148:
      drawRect(paramCanvas, 37, 210, 96, 254);
      break label148:
      if (this.hiscoMemory[i].map == this.highscore_choice_map)
      {
        label278: drawNum(this.hiscoMemory[i].score, bm_heartcount, 130 + (scr_w - this.bm_hight_score.getWidth() >> 1), 105 + 25 * (i % 6) + (scr_h - this.bm_hight_score.getHeight() >> 1), paramCanvas);
        drawNum(this.hiscoMemory[i].wave, bm_heartcount, 325 + (scr_w - this.bm_hight_score.getWidth() >> 1), 105 + 25 * (i % 6) + (scr_h - this.bm_hight_score.getHeight() >> 1), paramCanvas);
      }
      ++i;
      break label150:
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 398 + (scr_w - this.bm_hight_score.getWidth() >> 1), 30 + (scr_h - this.bm_hight_score.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void drawLogo(Canvas paramCanvas)
  {
    if (this.bm_logos == null)
    {
      this.bm_logos = new Bitmap[2];
      initLogoImg();
      label19: if (this.logo_time <= 30)
        break label86;
      Tool.draw_bitmap(paramCanvas, this.p, this.bm_logos[1], scr_w >> 1, scr_h >> 1, 3);
    }
    while (true)
    {
      return;
      if (this.bm_logos[0] == null)
        initLogoImg();
      if (this.bm_logos[1] == null);
      initLogoImg();
      break label19:
      label86: Tool.draw_bitmap(paramCanvas, this.p, this.bm_logos[0], scr_w >> 1, scr_h >> 1, 3);
    }
  }

  private void drawMainMenu(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.act_menu == null)
      initMainMenuImg();
    this.act_menu.draw(paramCanvas, this.p);
    if (!this.act_menu.isActionEnd())
      return;
    if (this.bm_menu_z == null)
      initMainMenuImg();
    paramCanvas.save();
    paramCanvas.clipRect((scr_w >> 1) - 50, 105 + (scr_h >> 1), 50 + (scr_w >> 1), 131 + (scr_h >> 1));
    paramCanvas.drawBitmap(this.bm_menu_z, this.menuz_x - 3 + (scr_w - 480 >> 1), this.menuz_y + (scr_h - 320 >> 1), this.p);
    paramCanvas.restore();
  }

  private void drawOptions(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (bm_options == null)
      initOptions();
    Tool.draw_bitmap(paramCanvas, this.p, bm_options, scr_w >> 1, scr_h >> 1, 3);
    if (!is_playmusic)
    {
      this.p.setStyle(Paint.Style.FILL);
      this.p.setColor(-12170355);
      paramCanvas.drawRect(280 + (scr_w - bm_options.getWidth() >> 1), 96 + (scr_h - bm_options.getHeight() >> 1), 303 + (scr_w - bm_options.getWidth() >> 1), 119 + (scr_h - bm_options.getHeight() >> 1), this.p);
      this.p.setStyle(Paint.Style.STROKE);
    }
    if (!is_shake)
    {
      this.p.setStyle(Paint.Style.FILL);
      this.p.setColor(-12170355);
      paramCanvas.drawRect(280 + (scr_w - bm_options.getWidth() >> 1), 155 + (scr_h - bm_options.getHeight() >> 1), 303 + (scr_w - bm_options.getWidth() >> 1), 178 + (scr_h - bm_options.getHeight() >> 1), this.p);
      this.p.setStyle(Paint.Style.STROKE);
    }
    if (this.options_time >= 0)
    {
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.options_selected)
      {
      default:
      case 0:
      case 1:
      }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - bm_options.getWidth() >> 1), 29 + (scr_h - bm_options.getHeight() >> 1), 0, 270, 30, 32, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 201 + (scr_w - bm_options.getWidth() >> 1), 213 + (scr_h - bm_options.getHeight() >> 1), 277, 471, 70, 32, 0);
    }
  }

  private void drawQuit(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (bm_exit == null)
      bm_exit = tool.creat_bitmap(2130837548);
    Tool.draw_bitmap(paramCanvas, this.p, bm_exit, scr_w >> 1, scr_h >> 1, 3);
    if (this.quit_time >= 0)
    {
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.quit_selected)
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 0:
      }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 167 + (scr_w - bm_exit.getWidth() >> 1), 82 + (scr_h - bm_exit.getHeight() >> 1), 0, 304, 240, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 163 + (scr_w - bm_exit.getWidth() >> 1), 126 + (scr_h - bm_exit.getHeight() >> 1), 0, 335, 240, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 115 + (scr_w - bm_exit.getWidth() >> 1), 173 + (scr_h - bm_exit.getHeight() >> 1), 0, 368, 240, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 117 + (scr_w - bm_exit.getWidth() >> 1), 216 + (scr_h - bm_exit.getHeight() >> 1), 0, 398, 240, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 396 + (scr_w - bm_exit.getWidth() >> 1), 29 + (scr_h - bm_exit.getHeight() >> 1), 0, 270, 30, 32, 0);
    }
  }

  private void drawSure(Canvas paramCanvas)
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    Tool.draw_bitmap(paramCanvas, this.p, bm_main_menu, scr_w >> 1, scr_h >> 1, 3);
    if (this.bm_sure == null)
      initSureImg();
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_sure, scr_w >> 1, scr_h >> 1, 3);
    if (this.sureNewGame_time >= 0)
    {
      if (bm_all_b == null)
        bm_all_b = tool.creat_bitmap(2130837505);
      switch (this.sureNewGame_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 118 + (scr_w - this.bm_sure.getWidth() >> 1), 202 + (scr_h - this.bm_sure.getHeight() >> 1), 232, 8, 90, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 270 + (scr_w - this.bm_sure.getWidth() >> 1), 202 + (scr_h - this.bm_sure.getHeight() >> 1), 232, 47, 90, 30, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, bm_all_b, 398 + (scr_w - this.bm_sure.getWidth() >> 1), 29 + (scr_h - this.bm_sure.getHeight() >> 1), 0, 270, 30, 33, 0);
    }
  }

  private void load()
  {
    this.gi = readData("pandoraDefence", "save");
    if (this.gi == null)
    {
      this.toast.cancel();
      this.toast.setText("没有记录");
      this.toast.show();
      isReading = false;
    }
    while (true)
    {
      return;
      outImg();
      difficulty = this.gi.difficulty;
      mode = this.gi.mode;
      choice_map = this.gi.choice_map;
      initGameWithoutMusic();
      map = this.gi.map;
      off_x = this.gi.off_x;
      off_y = this.gi.off_y;
      fresh_time = this.gi.fresh_time;
      refresh_time = this.gi.refresh_time;
      next_wave = this.gi.next_wave;
      heart = this.gi.heart;
      score = this.gi.score;
      wave = this.gi.wave;
      money = this.gi.money;
      is_faster = this.gi.is_faster;
      startX = this.gi.startX;
      startY = this.gi.startY;
      loadNpc(this.gi);
      loadTower(this.gi);
      initMusic();
      state = 7;
      isReading = false;
    }
  }

  private void logicAbout()
  {
    if (this.about_time >= 0)
    {
      this.about_time = (1 + this.about_time);
      if (this.about_time >= 5)
      {
        this.about_time = -1;
        switch (this.about_select)
        {
        default:
        case 0:
        }
      }
    }
    while (true)
    {
      return;
      state = 1;
    }
  }

  private void logicHelp()
  {
    if (this.help_time >= 0)
      this.help_time = (1 + this.help_time);
    if (this.help_time >= 5)
    {
      this.help_time = -1;
      switch (this.help_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return;
      this.help_index -= 1;
      if (this.help_index >= 0)
        continue;
      this.help_index = 0;
      continue;
      this.help_index = (1 + this.help_index);
      if (this.help_index <= 2)
        continue;
      this.help_index = 2;
      continue;
      state = 1;
    }
  }

  private void logicHightScore()
  {
    if (this.high_time >= 0)
      this.high_time = (1 + this.high_time);
    if (this.high_time >= 5)
    {
      this.high_time = -1;
      switch (this.high_selected)
      {
      default:
      case 0:
      }
    }
    while (true)
    {
      return;
      if (last_state == 7)
        outImg();
      state = last_state;
    }
  }

  private void logicQuit()
  {
    if (this.quit_time >= 0)
      this.quit_time = (1 + this.quit_time);
    if (this.quit_time >= 5)
    {
      this.quit_time = -1;
      switch (this.quit_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    while (true)
    {
      return;
      outImg();
      state = 7;
      continue;
      this.game_pause = false;
      outGame();
      outImg();
      initGameWithoutMusic();
      state = 7;
      continue;
      initOptions();
      state = 12;
      continue;
      initHightScore();
      initHightScoreImg();
      last_state = 7;
      state = 5;
      continue;
      save();
      mp_music.pause();
      mp_music = tool.creatMediaPlayer(2130968586);
      mp_music.setLooping(true);
      if (is_playmusic);
      try
      {
        mp_music.prepare();
        if (!will_not_play_music)
          label180: mp_music.start();
        this.is_game_start = false;
        outGame();
        initMainMenuImg();
        initMainMenu();
        state = 1;
        save_isFirstRun(state);
      }
      catch (Exception localException)
      {
        break label180:
      }
    }
  }

  public void all_get_path()
  {
    int i = 0;
    if (i >= npc.length)
      label2: return;
    if ((npc[i] == null) || (npc[i].state != 1));
    while (true)
    {
      ++i;
      break label2:
      npc[i].get_path();
    }
  }

  public void buildTower(int paramInt1, int paramInt2)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= towers.size())
        towers.add(new Tower(this.res, bmpTower[this.selectedTowerType], paramInt1, paramInt2, this.selectedTowerType));
      while (true)
      {
        return;
        if (towers.get(i) != null)
          break;
        towers.add(new Tower(this.res, bmpTower[this.selectedTowerType], paramInt1, paramInt2, this.selectedTowerType));
      }
    }
  }

  public void checkLock()
  {
    switch (choice_map)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return;
      if ((wave >= 100) && (!is_mapDust_open))
      {
        is_mapDust_open = true;
        if (this.toast != null)
          this.toast.cancel();
        this.toast.setText("新地图解锁");
        this.toast.show();
      }
      if ((wave < 50) || (is_mapICC_unlimit_open))
        continue;
      is_mapICC_unlimit_open = true;
      if (this.toast != null)
        this.toast.cancel();
      this.toast.setText("新模式解锁");
      this.toast.show();
      continue;
      if ((wave >= 100) && (!is_mapForest_open))
      {
        is_mapForest_open = true;
        if (this.toast != null)
          this.toast.cancel();
        this.toast.setText("新地图解锁");
        this.toast.show();
      }
      if ((wave < 50) || (is_mapDust_unlimit_open))
        continue;
      is_mapDust_unlimit_open = true;
      if (this.toast != null)
        this.toast.cancel();
      this.toast.setText("新模式解锁");
      this.toast.show();
      continue;
      if ((wave >= 100) && (!is_mapHill_open))
      {
        is_mapHill_open = true;
        if (this.toast != null)
          this.toast.cancel();
        this.toast.setText("新地图解锁");
        this.toast.show();
      }
      if ((wave < 50) || (is_mapForest_unlimit_open))
        continue;
      is_mapForest_unlimit_open = true;
      if (this.toast != null)
        this.toast.cancel();
      this.toast.setText("新模式解锁");
      this.toast.show();
      continue;
      if ((wave < 50) || (is_mapHill_unlimit_open))
        continue;
      is_mapHill_unlimit_open = true;
      if (this.toast != null)
        this.toast.cancel();
      this.toast.setText("新模式解锁");
      this.toast.show();
    }
  }

  public void checkMoney()
  {
    if (money < Data.buildFee[0])
    {
      this.canBuildNum = 0;
      return;
    }
    for (int i = 0; ; ++i)
    {
      if (i < Data.buildFee.length);
      if (money < Data.buildFee[i])
        continue;
      this.canBuildNum = (i + 1);
    }
  }

  public boolean check_can_build_in_path(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (towers.size() > 114);
    for (int i = 0; ; i = 1)
    {
      while (true)
      {
        label14: return i;
        if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= map.length) || (paramInt2 >= map[paramInt1].length))
          i = 0;
        if (map[paramInt1][paramInt2] != 0)
          i = 0;
        if (!check_can_build_in_to_out(in_0[1], in_0[0], out_0[1], out_0[0], paramInt1, paramInt2, paramBoolean))
          i = 0;
        if (((in_0[0] == in_1[0]) && (in_0[1] == in_1[1]) && (out_0[0] == out_1[0]) && (out_0[1] == out_1[1])) || (check_can_build_in_to_out(in_1[1], in_1[0], out_1[1], out_1[0], paramInt1, paramInt2, paramBoolean)))
          break;
        i = 0;
      }
      if ((npc[0] == null) || ((npc[0].kind != 3) && (npc[0].kind != 5)))
        break;
    }
    int j = 0;
    for (int k = 0; ; ++k)
    {
      if (k >= npc.length)
        i = 1;
      if ((npc[k] != null) && (npc[k].state == 1))
        break;
    }
    int l = 0;
    if (l >= npc[k].path.length);
    for (int i1 = 0; ; ++i1)
    {
      if (i1 < npc[k].path.length);
      if ((npc[k].path[i1][0] != paramInt1) || (npc[k].path[i1][1] != paramInt2))
        continue;
      if (j >= i1);
      map[paramInt1][paramInt2] = 1;
      int[][] arrayOfInt = npc[k].path;
      if (npc[k].get_path() == null)
      {
        map[paramInt1][paramInt2] = 0;
        npc[k].path = arrayOfInt;
        i = 0;
        break label14:
        if ((npc[k].path[l][0] == npc[k].get_row(npc[k].y)) && (npc[k].path[l][1] == npc[k].get_col(npc[k].x)))
          j = l;
        ++l;
      }
      npc[k].path = arrayOfInt;
      map[paramInt1][paramInt2] = 0;
    }
  }

  public boolean check_can_build_in_to_out(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
  {
    if ((paramInt5 == in_0[0]) && (paramInt6 == in_0[1]));
    for (int i = 0; ; i = 0)
    {
      while (true)
      {
        label23: return i;
        if ((paramInt5 == in_1[0]) && (paramInt6 == in_1[1]))
          i = 0;
        if ((paramInt5 != out_0[0]) || (paramInt6 != out_0[1]))
          break;
        i = 0;
      }
      if ((paramInt5 != out_1[0]) || (paramInt6 != out_1[1]))
        break;
    }
    AStar localAStar = new AStar();
    try
    {
      localAStar.setMap(map, Data.map_logic[0].length, Data.map_logic.length);
      label131: map[paramInt5][paramInt6] = 1;
      localAStar.setMap(map, map[0].length, map.length);
      int[][] arrayOfInt = localAStar.getPath(paramInt1, paramInt2, paramInt3, paramInt4);
      if (arrayOfInt == null)
      {
        map[paramInt5][paramInt6] = 0;
        i = 0;
      }
      int j;
      if (paramBoolean)
      {
        j = 0;
        if (j < npc.length)
          break label225;
      }
      map[paramInt5][paramInt6] = 0;
      i = 1;
      break label23:
      label225: npc[j].path = arrayOfInt;
      ++j;
    }
    catch (Exception localException)
    {
      break label131:
    }
  }

  public void check_win_lose()
  {
    if (((mode != 2) && (wave >= 100)) || (heart <= 0))
    {
      this.game_pause = true;
      state = 5;
      last_state = 1;
      outGame();
      initHightScore();
      saveScore();
      mp_music.pause();
      mp_music = tool.creatMediaPlayer(2130968586);
      mp_music.setLooping(true);
      if (!is_playmusic);
    }
    try
    {
      mp_music.prepare();
      mp_music.start();
      this.game_pause = false;
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void cleanImg()
  {
    int i = 0;
    int k;
    if (i >= bm_npc.length)
    {
      k = 0;
      if (k < bm_npc_green.length)
        break label118;
    }
    label75: label118: label121: int i2;
    for (int i1 = 0; ; ++i1)
    {
      if (i1 >= bm_npc_red.length)
      {
        bm_map = null;
        bm_faster = null;
        bm_fasterer = null;
        bm_pause = null;
        bm_resume = null;
        bm_setting = null;
        bm_round = null;
        bm_roundcount = null;
        bm_moneycount = null;
        bmpBuildFeeNum = null;
        return;
        int j = 0;
        if (j >= bm_npc[i].length)
          ++i;
        if (bm_npc[i][j] == null);
        while (true)
        {
          ++j;
          break label75:
          bm_npc[i][j] = null;
        }
        int l = 0;
        if (l >= bm_npc_green[k].length)
          ++k;
        if (bm_npc_green[k][l] == null);
        while (true)
        {
          ++l;
          break label121:
          bm_npc_green[k][l] = null;
        }
      }
      i2 = 0;
      label170: if (i2 < bm_npc_red[i1].length)
        break;
    }
    if (bm_npc_red[i1][i2] == null);
    while (true)
    {
      ++i2;
      break label170:
      bm_npc_red[i1][i2] = null;
    }
  }

  public void cleanNpcAndTower()
  {
    int j;
    if (npc != null)
    {
      j = 0;
      if (j < npc.length)
        break label67;
      npc = null;
    }
    if (towers != null);
    for (int i = towers.size() - 1; ; --i)
    {
      if (i < 0)
      {
        towers = null;
        if (tower != null)
          tower = null;
        if (map != null)
          map = null;
        System.gc();
        return;
        label67: npc[j] = null;
        ++j;
      }
      towers.remove(i);
    }
  }

  public void doPause()
  {
    if (mp_music != null);
    try
    {
      mp_music.pause();
      this.game_pause = true;
      Log.v("[@lcnb]", "game_pause = true");
      this.pause = true;
      return;
    }
    catch (Exception localException)
    {
      mp_music = null;
    }
  }

  public void drawBuildGird(Canvas paramCanvas)
  {
    int j;
    for (int i = 0; ; ++i)
    {
      if (i >= 12)
      {
        this.p.reset();
        return;
      }
      j = 0;
      label18: if (j < 19)
        break;
    }
    if ((i + j) % 2 == 0)
      this.p.setColor(-16711936);
    while (true)
    {
      this.p.setAlpha(30);
      paramCanvas.drawRect(84 + j * 48 + off_x, 72 + i * 48 + off_y, 84 + (48 + j * 48) + off_x, 72 + (48 + i * 48) + off_y, this.p);
      ++j;
      break label18:
      this.p.setColor(-16776961);
    }
  }

  public void drawNum(int paramInt1, Bitmap paramBitmap, int paramInt2, int paramInt3, Canvas paramCanvas)
  {
    int i = paramBitmap.getWidth() / 10;
    int j = paramBitmap.getHeight();
    int k = num_length(paramInt1);
    for (int l = 0; ; ++l)
    {
      if (l >= k)
        return;
      paramCanvas.save();
      Tool.setClip(paramCanvas, paramInt2 + l * i, paramInt3, i, j);
      int i1 = paramInt1 / Math.max((int)Math.pow(10.0D, k - 1 - l), 1) % 10;
      paramCanvas.drawBitmap(paramBitmap, paramInt2 + (l * i - i1 * i), paramInt3, this.p);
      paramCanvas.restore();
    }
  }

  public void drawRect(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramCanvas.drawRect(paramInt1 + (scr_w - this.bm_hight_score.getWidth() >> 1), paramInt2 + (scr_h - this.bm_hight_score.getHeight() >> 1), paramInt3 + (scr_w - this.bm_hight_score.getWidth() >> 1), paramInt4 + (scr_h - this.bm_hight_score.getHeight() >> 1), this.p);
  }

  public void drawSpriteAfterSort(Canvas paramCanvas)
  {
    Vector localVector = new Vector();
    int i = 0;
    int j = 0;
    label13: int k;
    label27: Sprite[] arrayOfSprite;
    int l;
    if (j >= towers.size())
    {
      k = 0;
      if (k < npc.length)
        break label238;
      arrayOfSprite = new Sprite[localVector.size()];
      l = 0;
      if (l < arrayOfSprite.length)
        break label324;
      if (arrayOfSprite.length > 0)
        arrayOfSprite = Tool.Sort(arrayOfSprite, 0, arrayOfSprite.length - 1);
      Log.v("[show]", "all_object.length : " + arrayOfSprite.length);
    }
    for (int i1 = 0; ; ++i1)
    {
      if (i1 >= arrayOfSprite.length)
      {
        return;
        if ((towers.get(j) == null) || (48 + ((Tower)towers.get(j)).getCenterX() < 0) || (((Tower)towers.get(j)).getCenterX() - 48 > scr_w) || (48 + ((Tower)towers.get(j)).getCenterY() < 0) || (((Tower)towers.get(j)).getCenterY() - 48 > scr_h));
        while (true)
        {
          ++j;
          break label13:
          localVector.add((Sprite)towers.get(j));
          ++i;
        }
        label238: if ((48 + npc[k].getCenterX() < 0) || (npc[k].getCenterX() - 48 > scr_w) || (48 + npc[k].getCenterY() < 0) || (npc[k].getCenterY() - 48 > scr_h));
        while (true)
        {
          ++k;
          break label27:
          localVector.add(npc[k]);
        }
        label324: arrayOfSprite[l] = ((Sprite)localVector.get(l));
        ++l;
      }
      arrayOfSprite[i1].paint(paramCanvas, this.p);
    }
  }

  public void drawUI(Canvas paramCanvas)
  {
    this.p.setAlpha(255);
    int i = startY;
    int j = 0;
    if (j >= towerCount)
    {
      label16: paramCanvas.drawBitmap(bmpMoney, 10.0F, 10.0F, this.p);
      drawNum(money, bm_moneycount, 10 + bmpMoney.getWidth(), 10 - (bm_moneycount.getHeight() - bmpMoney.getHeight()) / 2, paramCanvas);
      if (!is_faster)
        break label730;
      paramCanvas.drawBitmap(bm_fasterer, scr_w - bm_faster.getWidth() - 10, 20 + bmpHeart.getHeight() - 8, this.p);
      label122: paramCanvas.drawBitmap(bmpHeart, scr_w - bmpHeart.getWidth() - 10, 10.0F, this.p);
      if (heart >= 10)
        break label765;
      drawNum(heart, bm_heartcount, 8 + (scr_w - bmpHeart.getWidth()), 24, paramCanvas);
      label184: paramCanvas.drawBitmap(bm_setting, 5 + 2 * bm_pause.getWidth(), scr_h - bm_setting.getHeight() - 5, this.p);
      if (!this.game_pause)
        break label791;
      paramCanvas.drawBitmap(bm_resume, 20.0F, scr_h - bm_resume.getHeight() - 10, this.p);
    }
    while (true)
    {
      paramCanvas.drawBitmap(bm_round, (scr_w >> 1) - 45, 32.0F, this.p);
      Tool.drawImageNumber(paramCanvas, bm_roundcount, wave, 35 + (scr_w >> 1), 32, this.p);
      Tool.drawImageNumber(paramCanvas, bm_roundcount, score, (scr_w - Tool.getImageNumberWidth(score, bm_roundcount)) / 2, 5, this.p);
      return;
      int k = startX + j * bmpIconGooTower.getWidth();
      if ((j == this.selectedTowerType) && (this.isSelect))
      {
        Tool.setClip(paramCanvas, 5 + (k + 4), 5 + (i + 4), 41, bmpIconMoney.getHeight() - 10);
        paramCanvas.drawBitmap(bmpIconMoney, null, newRect(5 + (k + 4 - j * 51), 5 + (i + 4), bmpIconMoney.getWidth() - 10, bmpIconMoney.getHeight() - 10), this.p);
        paramCanvas.restore();
        paramCanvas.drawBitmap(bmpIconGooTower, null, newRect(k + 5, i + 5, bmpIconGooTower.getWidth() - 10, bmpIconGooTower.getHeight() - 10), this.p);
        if (j + 1 > this.canBuildNum)
        {
          label482: Tool.setClip(paramCanvas, k + 4, i + 4, 50, bmpIconNoMoney.getHeight());
          paramCanvas.drawBitmap(bmpIconNoMoney, k + 4 - j * 50, i + 4, this.p);
          paramCanvas.restore();
          paramCanvas.drawBitmap(bmpIconGooTower, k, i, this.p);
        }
        if (Tower.buildFee[j] >= 10)
          break label664;
        Tool.drawImageNumber(paramCanvas, bmpBuildFeeNum, Tower.buildFee[j], k + 24, i + 39, this.p);
      }
      while (true)
      {
        ++j;
        break label16:
        Tool.setClip(paramCanvas, k + 4, i + 4, 50, bmpIconMoney.getHeight());
        paramCanvas.drawBitmap(bmpIconMoney, k + 4 - j * 50, i + 4, this.p);
        paramCanvas.restore();
        paramCanvas.drawBitmap(bmpIconGooTower, k, i, this.p);
        break label482:
        if (Tower.buildFee[j] < 100)
          label664: Tool.drawImageNumber(paramCanvas, bmpBuildFeeNum, Tower.buildFee[j], k + 21, i + 39, this.p);
        Tool.drawImageNumber(paramCanvas, bmpBuildFeeNum, Tower.buildFee[j], k + 17, i + 39, this.p);
      }
      label730: paramCanvas.drawBitmap(bm_faster, scr_w - bm_faster.getWidth(), 20 + bmpHeart.getHeight(), this.p);
      break label122:
      label765: drawNum(heart, bm_heartcount, scr_w - bmpHeart.getWidth(), 21, paramCanvas);
      break label184:
      label791: paramCanvas.drawBitmap(bm_pause, 20.0F, scr_h - bm_pause.getHeight() - 5, this.p);
    }
  }

  public boolean getExit()
  {
    return this.is_exit;
  }

  public void get_path()
  {
    int i = 0;
    if (i >= npc.length);
    int j;
    do
    {
      return;
      if ((npc[i] == null) || (npc[i].state != 1))
        ++i;
      if (npc[i].path == null)
        npc[i].path = npc[i].get_path();
      label66: j = 0;
    }
    while (j >= npc.length);
    if (npc[j] == null);
    while (true)
    {
      ++j;
      break label66:
      if (npc[j].path != null)
        continue;
      npc[j].path = npc[i].path;
    }
  }

  public void initAbout()
  {
  }

  public void initAboutImg()
  {
    if (this.bm_about != null)
      return;
    this.bm_about = BitmapFactory.decodeResource(getResources(), 2130837504);
  }

  public void initChoiceMap()
  {
    this.toast.setText("请选择地图");
    this.toast.show();
  }

  public void initChoiceMapImg()
  {
    if (this.bm_choice_map != null)
      return;
    this.bm_choice_map = tool.creat_bitmap(2130837552);
  }

  public void initChoiceMode()
  {
    mode = 0;
    if (this.toast != null)
      this.toast.cancel();
    if (bm_choice_mode != null)
      return;
    bm_choice_mode = tool.creat_bitmap(2130837553);
  }

  public void initGame()
  {
    this.is_game_start = true;
    wave = 0;
    score = 0;
    money = 10;
    heart = 20;
    is_faster = false;
    tower = new Tower();
    towers = new ArrayList();
    off_x = 0;
    off_y = scr_h - 720 >> 1;
    loadImage();
    initMap();
    initNpc();
    initMusic();
    if (this.toast == null)
      return;
    this.toast.cancel();
  }

  public void initGameWithoutMusic()
  {
    this.is_game_start = true;
    wave = 0;
    score = 0;
    money = 10;
    heart = 20;
    is_faster = false;
    tower = new Tower();
    towers = new ArrayList();
    off_x = 0;
    off_y = scr_h - 720 >> 1;
    loadImage();
    initMap();
    initNpc();
    if (this.toast == null)
      return;
    this.toast.cancel();
  }

  public void initHelp()
  {
  }

  public void initHelpImg()
  {
    if (this.bm_help_image == null)
      this.bm_help_image = tool.creat_bitmap(2130837519);
    if (this.bm_help != null)
      return;
    this.bm_help = BitmapFactory.decodeResource(getResources(), 2130837518);
  }

  public void initHightScore()
  {
    if ((this.hiscoMemory == null) || (this.hiscoMemory[0] == null))
      this.hiscoMemory = new HightScoreMemory[24];
    for (int i = 0; ; ++i)
    {
      if (i >= this.hiscoMemory.length)
      {
        loadScore();
        state = 5;
        return;
      }
      this.hiscoMemory[i] = new HightScoreMemory();
    }
  }

  public void initHightScoreImg()
  {
    if (this.bm_hight_score == null)
      this.bm_hight_score = tool.creat_bitmap(2130837520);
    if (bm_heartcount != null)
      return;
    bm_heartcount = tool.creat_bitmap(2130837517);
  }

  public void initLogo()
  {
    mp_music = tool.creatMediaPlayer(2130968586);
    mp_music.stop();
    mp_music.setLooping(true);
    outGame();
  }

  public void initLogoImg()
  {
    if (this.bm_logos[0] == null)
      this.bm_logos[0] = tool.creat_bitmap(2130837527);
    if (this.bm_logos[1] != null)
      return;
    this.bm_logos[1] = tool.creat_bitmap(2130837527);
  }

  public void initMainMenu()
  {
    this.menu_move_right = false;
    this.menu_move_left = false;
    this.menuz_x = 190;
    this.menuz_y = 265;
    if ((is_playmusic) && (!will_not_play_music));
    try
    {
      mp_music.prepare();
      mp_music.start();
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void initMainMenuImg()
  {
    if (bm_main == null)
      bm_main = tool.creat_bitmap(2130837533);
    if (this.bm_menu_z == null)
      this.bm_menu_z = tool.creat_bitmap(2130837534);
    if (this.act_menu == null)
      this.act_menu = new Action(this.res, bm_main, 2130968585);
    this.act_menu.setPosition(scr_w >> 1, scr_h >> 1);
    this.act_menu.setAction(0);
  }

  public void initMap()
  {
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[0] = 12;
    arrayOfInt1[1] = 19;
    map = (byte[][])Array.newInstance(Byte.TYPE, arrayOfInt1);
    int i = 0;
    if (i >= map.length)
      switch (choice_map)
      {
      default:
        label72: switch (choice_map)
        {
        default:
        case 0:
        case 1:
        case 2:
        case 3:
        }
      case 0:
      case 1:
      case 2:
      case 3:
      }
    while (true)
    {
      return;
      for (int j = 0; ; ++j)
      {
        if (j >= map[i].length)
          ++i;
        map[i][j] = Data.map_logic[(choice_map / 3)][i][j];
      }
      bm_map = tool.creat_bitmap(2130837529);
      break label72:
      bm_map = tool.creat_bitmap(2130837530);
      break label72:
      bm_map = tool.creat_bitmap(2130837531);
      break label72:
      bm_map = tool.creat_bitmap(2130837532);
      break label72:
      int[] arrayOfInt8 = in_0;
      in_1[0] = 5;
      arrayOfInt8[0] = 5;
      int[] arrayOfInt9 = in_0;
      in_1[1] = 0;
      arrayOfInt9[1] = 0;
      int[] arrayOfInt10 = out_0;
      out_1[0] = 5;
      arrayOfInt10[0] = 5;
      int[] arrayOfInt11 = out_0;
      out_1[1] = 18;
      arrayOfInt11[1] = 18;
      continue;
      in_0[0] = 6;
      in_0[1] = 0;
      in_1[0] = 0;
      in_1[1] = 9;
      int[] arrayOfInt6 = out_0;
      out_1[0] = 6;
      arrayOfInt6[0] = 6;
      int[] arrayOfInt7 = out_0;
      out_1[1] = 18;
      arrayOfInt7[1] = 18;
      continue;
      in_0[0] = 6;
      in_0[1] = 0;
      in_1[0] = 0;
      in_1[1] = 9;
      out_0[0] = 6;
      out_0[1] = 18;
      out_1[0] = 11;
      out_1[1] = 9;
      continue;
      int[] arrayOfInt2 = in_0;
      in_1[0] = 5;
      arrayOfInt2[0] = 5;
      int[] arrayOfInt3 = in_0;
      in_1[1] = 0;
      arrayOfInt3[1] = 0;
      int[] arrayOfInt4 = out_0;
      out_1[0] = 5;
      arrayOfInt4[0] = 5;
      int[] arrayOfInt5 = out_0;
      out_1[1] = 18;
      arrayOfInt5[1] = 18;
    }
  }

  public void initMoreSplendid()
  {
    MainActivity.mainActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://a.d.cn")));
    this.is_exit = true;
    will_music = true;
    saveMenu();
    Process.killProcess(Process.myPid());
  }

  public void initMusic()
  {
    if (mp_music != null)
    {
      mp_music.stop();
      mp_music = null;
    }
    mp_music = tool.creatMediaPlayer(2130968583);
    mp_music.setLooping(true);
    if ((is_playmusic) && (!will_not_play_music));
    try
    {
      mp_music.prepare();
      mp_music.start();
      if (mp_gun == null)
        mp_gun = tool.creatMediaPlayer(2130968584);
      if (mp_slowdown == null)
        mp_slowdown = tool.creatMediaPlayer(2130968598);
      if (mp_flood == null)
        mp_flood = tool.creatMediaPlayer(2130968582);
      if (mp_roket == null)
        mp_roket = tool.creatMediaPlayer(2130968597);
      if (mp_fire == null)
        mp_fire = tool.creatMediaPlayer(2130968581);
      if (mp_boomb == null)
        mp_boomb = tool.creatMediaPlayer(2130968580);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void initNpc()
  {
    if (npc == null)
      npc = new Npc[20];
    for (int i = 0; ; ++i)
    {
      if (i >= npc.length)
        return;
      if (npc[i] != null)
        continue;
      npc[i] = new Npc(this.res, bm_npc, bm_npc_red, bm_npc_green, this.npc_actionFile);
      npc[i].state = 3;
    }
  }

  public void initOptions()
  {
    if (bm_options != null)
      return;
    bm_options = tool.creat_bitmap(2130837547);
  }

  public void initQuitImg()
  {
    if (bm_exit != null)
      return;
    bm_exit = tool.creat_bitmap(2130837548);
  }

  public void initSure()
  {
  }

  public void initSureImg()
  {
    if (this.bm_sure != null)
      return;
    this.bm_sure = tool.creat_bitmap(2130837554);
  }

  public void initTotalImg()
  {
    if (bm_main_menu == null)
      bm_main_menu = tool.initBitmap(2130837528, Math.max(scr_w, scr_h), Math.min(scr_w, scr_h));
    if (bm_all_b != null)
      return;
    bm_all_b = tool.creat_bitmap(2130837505);
  }

  public void loadImage()
  {
    if (bm_npc[0][0] == null)
      bm_npc[0][0] = tool.creat_bitmap(2130837537);
    if (bm_npc[0][1] == null)
      bm_npc[0][1] = tool.creat_bitmap(2130837538);
    if (bm_npc[1][0] == null)
      bm_npc[1][0] = tool.creat_bitmap(2130837545);
    if (bm_npc[2][0] == null)
      bm_npc[2][0] = tool.creat_bitmap(2130837539);
    if (bm_npc[2][1] == null)
      bm_npc[2][1] = tool.creat_bitmap(2130837541);
    if (bm_npc[3][0] == null)
      bm_npc[3][0] = tool.creat_bitmap(2130837542);
    if (bm_npc[3][1] == null)
      bm_npc[3][1] = tool.creat_bitmap(2130837543);
    if (bm_npc[4][0] == null)
      bm_npc[4][0] = tool.creat_bitmap(2130837544);
    if (bm_npc[5][0] == null)
      bm_npc[5][0] = tool.creat_bitmap(2130837540);
    Canvas localCanvas = new Canvas();
    Paint localPaint = new Paint();
    int i = 0;
    if (i >= bm_npc_red.length)
    {
      if (bmpBulletSlowdown == null)
        bmpBulletSlowdown = tool.creat_bitmap(2130837509);
      if (bmpBulletBoom == null)
        bmpBulletBoom = tool.creat_bitmap(2130837507);
      if (bmpBulletBomb == null)
        bmpBulletBomb = tool.creat_bitmap(2130837506);
      if (bmpBulletFire == null)
        bmpBulletFire = tool.creat_bitmap(2130837508);
      if (bmpMoveTower == null)
        bmpMoveTower = tool.creat_bitmap(2130837555);
      if (bmpBuildFeeNum == null)
        bmpBuildFeeNum = tool.creat_bitmap(2130837510);
      if (bmpIconGooTower == null)
        bmpIconGooTower = tool.creat_bitmap(2130837522);
      if (bmp_update == null)
        bmp_update = tool.creat_bitmap(2130837526);
      if (bmp_mai == null)
        bmp_mai = tool.creat_bitmap(2130837525);
      if (bmpIconNoMoney == null)
        bmpIconNoMoney = tool.creat_bitmap(2130837524);
      if (bmpIconMoney == null)
        bmpIconMoney = tool.creat_bitmap(2130837523);
      if (bmpMoney == null)
        bmpMoney = tool.creat_bitmap(2130837535);
      if (bmpHeart == null)
        bmpHeart = tool.creat_bitmap(2130837516);
      if (bm_faster == null)
        bm_faster = tool.creat_bitmap(2130837511);
      if (bm_fasterer == null)
        bm_fasterer = tool.creat_bitmap(2130837512);
      if (bm_pause == null)
        bm_pause = tool.creat_bitmap(2130837513);
      if (bm_resume == null)
        bm_resume = tool.creat_bitmap(2130837514);
      if (bm_setting == null)
        bm_setting = tool.creat_bitmap(2130837515);
      if (bm_round == null)
        bm_round = tool.creat_bitmap(2130837549);
      if (bm_roundcount == null)
        bm_roundcount = tool.creat_bitmap(2130837550);
      if (bm_moneycount == null)
        bm_moneycount = tool.creat_bitmap(2130837536);
      if (bm_heartcount == null)
        bm_heartcount = tool.creat_bitmap(2130837517);
      if (mode != 0)
        break label936;
      towerCount = 4;
      bmpTower = null;
      bmpTower = new Bitmap[towerCount];
    }
    for (int k = 0; ; ++k)
    {
      if (k >= towerCount)
      {
        startX = scr_w - bmpIconGooTower.getWidth() * towerCount - 10;
        startY = scr_h - bmpIconGooTower.getHeight();
        this.npc_actionFile[0][0] = 2130968587;
        this.npc_actionFile[0][1] = 2130968588;
        this.npc_actionFile[1][0] = 2130968595;
        this.npc_actionFile[2][0] = 2130968589;
        this.npc_actionFile[2][1] = 2130968591;
        this.npc_actionFile[3][0] = 2130968592;
        this.npc_actionFile[3][1] = 2130968593;
        this.npc_actionFile[4][0] = 2130968594;
        this.npc_actionFile[5][0] = 2130968590;
        System.gc();
        return;
        int j = 0;
        if (j >= bm_npc_red[0].length)
          label829: ++i;
        if (bm_npc[i][j] == null);
        while (true)
        {
          ++j;
          break label829:
          if (bm_npc_red[i][j] == null)
            bm_npc_red[i][j] = Tool.initBitmapRedAndGreen(bm_npc[i][j], -65536, localCanvas, localPaint);
          if (bm_npc_green[i][j] != null)
            continue;
          bm_npc_green[i][j] = Tool.initBitmapRedAndGreen(bm_npc[i][j], -16711936, localCanvas, localPaint);
        }
        label936: towerCount = 6;
      }
      bmpTower[k] = tool.creat_bitmap(2130837556 + k);
    }
  }

  public void loadNpc(GameIo paramGameIo)
  {
    int i = 0;
    if (i >= npc.length)
      label2: return;
    npc[i].path = paramGameIo.npc_path[i];
    npc[i].actionFile = paramGameIo.npc_actionFile[i];
    npc[i].x = paramGameIo.npc_x[i];
    npc[i].y = paramGameIo.npc_y[i];
    npc[i].row = paramGameIo.npc_row[i];
    npc[i].col = paramGameIo.npc_col[i];
    npc[i].next_row = paramGameIo.npc_next_row[i];
    npc[i].next_col = paramGameIo.npc_next_col[i];
    npc[i].state = paramGameIo.npc_state[i];
    npc[i].next_dir = paramGameIo.npc_next_dir[i];
    npc[i].dir = paramGameIo.npc_dir[i];
    npc[i].time = paramGameIo.npc_time[i];
    npc[i].dest_row = paramGameIo.npc_dest_row[i];
    npc[i].dest_col = paramGameIo.npc_dest_col[i];
    npc[i].dir_last = paramGameIo.npc_dir_last[i];
    npc[i].blood = paramGameIo.npc_blood[i];
    npc[i].off_blood = paramGameIo.npc_off_blood[i];
    npc[i].speed_down = paramGameIo.npc_speed_down[i];
    npc[i].speed = paramGameIo.npc_speed[i];
    npc[i].show_blood_time = paramGameIo.npc_show_blood_time[i];
    npc[i].slow_down_time = paramGameIo.npc_slow_down_time[i];
    npc[i].attack_kind = paramGameIo.npc_attack_kind[i];
    npc[i].die_time_count = paramGameIo.npc_die_time_count[i];
    npc[i].kind = paramGameIo.npc_kind[i];
    npc[i].kind2 = paramGameIo.npc_kind2[i];
    npc[i].in_direction = paramGameIo.npc_in_direction[i];
    npc[i].out_direction = paramGameIo.npc_out_direction[i];
    npc[i].act_npc.setActionFile(npc[i].actionFile[npc[i].kind][npc[i].kind2]);
    if (npc[i].speed_down == 0)
      npc[i].act_npc.setBitmap(npc[i].bm_npc_normal[npc[i].kind][npc[i].kind2]);
    while (true)
    {
      npc[i].act_npc.setAction(npc[i].dir);
      if (npc[i].state != 1)
        npc[i].act_npc.setAction(4 + npc[i].dir);
      ++i;
      break label2:
      npc[i].act_npc.setBitmap(npc[i].bm_npc_green[npc[i].kind][npc[i].kind2]);
    }
  }

  public void loadScore()
  {
    GameScoreIo localGameScoreIo = readScore("pandoraDefence", "score");
    if (localGameScoreIo == null)
    {
      this.hiscoMemory = HightScoreMemory.SYSTEM_HIGH_SCORE;
      return;
    }
    for (int i = 0; ; ++i)
    {
      if (i < this.hiscoMemory.length);
      this.hiscoMemory[i].map = localGameScoreIo.map[i];
      this.hiscoMemory[i].score = localGameScoreIo.score[i];
      this.hiscoMemory[i].wave = localGameScoreIo.wave[i];
    }
  }

  public void loadTower(GameIo paramGameIo)
  {
    towers = new ArrayList();
    int i = 0;
    if (i >= paramGameIo.tower_size)
      label12: return;
    Tower localTower = new Tower(getResources(), bmpTower[paramGameIo.tower_type[i]], paramGameIo.tower_x[i], paramGameIo.tower_y[i], paramGameIo.tower_type[i]);
    localTower.x = paramGameIo.tower_x[i];
    localTower.y = paramGameIo.tower_y[i];
    localTower.row = paramGameIo.tower_row[i];
    localTower.col = paramGameIo.tower_col[i];
    localTower.type = paramGameIo.tower_type[i];
    localTower.stage = paramGameIo.tower_stage[i];
    localTower.currentDir = paramGameIo.tower_currentDir[i];
    localTower.targetDir = paramGameIo.tower_targetDir[i];
    localTower.isPressTower = paramGameIo.tower_isPressTower[i];
    localTower.canBuild = paramGameIo.tower_canBuild[i];
    localTower.isChangeDir = paramGameIo.tower_isChangeDir[i];
    localTower.targetX = paramGameIo.tower_targetX[i];
    localTower.targetY = paramGameIo.tower_targetY[i];
    localTower.shotCon = paramGameIo.tower_shotCon[i];
    localTower.count = paramGameIo.tower_count[i];
    localTower.currentFrame = paramGameIo.tower_currentFrame[i];
    localTower.targetFrame = paramGameIo.tower_targetFrame[i];
    switch (localTower.type)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 4:
    case 3:
    case 5:
    }
    while (true)
    {
      towers.add(localTower);
      ++i;
      break label12:
      localTower.act.setAction(localTower.currentDir + 12 * localTower.stage);
      continue;
      localTower.act.setAction(localTower.stage);
      continue;
      localTower.act.setAction(2 * localTower.stage);
    }
  }

  public void load_isFirstRun()
  {
    IsFirstRun localIsFirstRun = readIsFirst("pandoraDefence", "is_first_run");
    if (localIsFirstRun == null);
    while (true)
    {
      return;
      state = localIsFirstRun.state;
      if ((state != 7) && (bm_main_menu == null))
        state = 0;
      if ((state != 7) || (bm_map != null))
        continue;
      state = 0;
    }
  }

  public void loadmenu()
  {
    GameMenuIo localGameMenuIo = readMenu("pandoraDefence", "menu");
    if (localGameMenuIo == null);
    while (true)
    {
      return;
      is_mapICC_unlimit_open = localGameMenuIo.is_mapICC_unlimit_open;
      is_mapDust_open = localGameMenuIo.is_mapDust_open;
      is_mapDust_unlimit_open = localGameMenuIo.is_mapDust_unlimit_open;
      is_mapForest_open = localGameMenuIo.is_mapForest_open;
      is_mapForest_unlimit_open = localGameMenuIo.is_mapForest_unlimit_open;
      is_mapHill_open = localGameMenuIo.is_mapHill_open;
      is_mapHill_unlimit_open = localGameMenuIo.is_mapHill_unlimit_open;
      is_playmusic = localGameMenuIo.is_playmusic;
      is_shake = localGameMenuIo.is_shake;
      will_music = localGameMenuIo.will_music;
    }
  }

  public void logic()
  {
    will_music = false;
    will_not_play_music = false;
    switch (state)
    {
    case 6:
    case 10:
    default:
    case 0:
    case 1:
    case 2:
    case 8:
    case 11:
    case 9:
    case 3:
    case 4:
    case 5:
    case 12:
    case 7:
    }
    int i;
    while (true)
    {
      return;
      this.logo_time = (1 + this.logo_time);
      if (this.logo_time <= 60)
        continue;
      state = 1;
      initMainMenu();
      for (int j = 0; ; ++j)
      {
        if (j >= this.bm_logos.length)
        {
          this.logo_time = 0;
          System.gc();
        }
        this.bm_logos[j] = null;
      }
      if (this.act_menu == null)
        initMainMenuImg();
      if (!this.act_menu.isActionEnd())
        this.act_menu.nextFrame();
      logicMainMenu();
      continue;
      logicSureNewGame();
      continue;
      logicChoiceMap();
      continue;
      logicQuit();
      continue;
      logicChoiceMode();
      continue;
      logicAbout();
      continue;
      logicHelp();
      continue;
      logicHightScore();
      continue;
      logicOptions();
      continue;
      if (this.game_pause)
        continue;
      checkMoney();
      touchEvent();
      off_xy();
      logic_npc();
      logic_game_refresh();
      get_path();
      if (sale_to_regetpath)
      {
        all_get_path();
        sale_to_regetpath = false;
      }
      i = 0;
      label277: if (i < towers.size())
        break;
      checkLock();
      check_win_lose();
    }
    if (towers.get(i) == null);
    while (true)
    {
      ++i;
      break label277:
      Tower localTower = (Tower)towers.get(i);
      localTower.setCamera();
      localTower.logic(npc);
      localTower.logic();
      if (localTower.anpc == null)
        continue;
      localTower.bullet.logic();
      localTower.bullet.setCamera();
    }
  }

  public void logicChoiceMap()
  {
    if (this.choiceMap_time >= 0)
      this.choiceMap_time = (1 + this.choiceMap_time);
    if (this.choiceMap_time >= 5)
    {
      this.choiceMap_time = -1;
      switch (this.choiceMap_selected)
      {
      default:
      case 4:
      case 0:
      case 1:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      return;
      state = 1;
      continue;
      initChoiceMode();
      choice_map = 0;
      state = 9;
      continue;
      initChoiceMode();
      choice_map = 1;
      state = 9;
      continue;
      initChoiceMode();
      choice_map = 2;
      state = 9;
      continue;
      initChoiceMode();
      choice_map = 3;
      state = 9;
    }
  }

  public void logicChoiceMode()
  {
    if (this.choiceMode_time >= 0)
      this.choiceMode_time = (1 + this.choiceMode_time);
    if (this.choiceMode_time >= 5)
    {
      this.choiceMode_time = -1;
      switch (this.choiceMode_selected)
      {
      default:
      case 0:
      case 1:
      }
    }
    while (true)
    {
      return;
      this.game_pause = false;
      outGame();
      outImg();
      initGame();
      state = 7;
      save_isFirstRun(state);
      bm_choice_mode = null;
      continue;
      state = 1;
      bm_choice_mode = null;
    }
  }

  public void logicMainMenu()
  {
    if (this.menu_move_right)
    {
      this.menuz_x = (8 + this.menuz_x);
      label18: if (this.menuz_x < 190)
        break label238;
      this.menuz_x = 190;
      label35: if ((Math.abs(50 + (this.menuz_x - 240)) % 100 <= 95) && (Math.abs(50 + (this.menuz_x - 240)) % 100 >= 5))
        break label406;
      if (50 + (this.menuz_x - 240) >= -550)
        break label258;
      this.mainMenu_selected = 6;
      label101: this.menuz_x = (190 - 100 * this.mainMenu_selected);
      this.mainmenu_canSelect = true;
      this.menu_move_left = false;
      this.menu_move_right = false;
      label131: if (this.mainMenu_time < 0)
        break label522;
      this.mainMenu_time = (1 + this.mainMenu_time);
      this.menuz_y = 238;
      if (this.mainMenu_time >= 5)
      {
        this.mainMenu_time = -1;
        switch (this.mainMenu_selected)
        {
        default:
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        }
      }
    }
    while (true)
    {
      return;
      if (this.menu_move_left);
      this.menuz_x -= 8;
      break label18:
      label238: if (this.menuz_x <= -410);
      this.menuz_x = -410;
      break label35:
      if (50 + (this.menuz_x - 240) < -450)
        label258: this.mainMenu_selected = 5;
      if (50 + (this.menuz_x - 240) < -350)
        this.mainMenu_selected = 4;
      if (50 + (this.menuz_x - 240) < -250)
        this.mainMenu_selected = 3;
      if (50 + (this.menuz_x - 240) < -150)
        this.mainMenu_selected = 2;
      if (50 + (this.menuz_x - 240) < -50)
        this.mainMenu_selected = 1;
      if (50 + (this.menuz_x - 240) < 50);
      this.mainMenu_selected = 0;
      break label101:
      label406: this.mainmenu_canSelect = false;
      break label131:
      initSure();
      state = 2;
      continue;
      if (isReading)
        continue;
      isReading = true;
      this.game_pause = false;
      Log.v("[@lcnb]", "prs resume !");
      load();
      save_isFirstRun(state);
      continue;
      initMoreSplendid();
      continue;
      initHightScore();
      last_state = 1;
      state = 5;
      continue;
      initHelp();
      state = 4;
      continue;
      initAbout();
      state = 3;
      continue;
      this.is_exit = true;
      MainActivity.mainActivity.exit();
      continue;
      label522: this.menuz_y = 265;
    }
  }

  public void logicOptions()
  {
    if (this.options_time >= 0)
      this.options_time = (1 + this.options_time);
    if (this.options_time >= 5)
    {
      this.options_time = -1;
      switch (this.options_selected)
      {
      default:
      case 0:
      case 1:
      }
    }
    while (true)
    {
      return;
      state = 11;
    }
  }

  public void logicSureNewGame()
  {
    if (this.sureNewGame_time >= 0)
      this.sureNewGame_time = (1 + this.sureNewGame_time);
    if (this.sureNewGame_time >= 10)
    {
      this.sureNewGame_time = -1;
      switch (this.sureNewGame_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return;
      initChoiceMap();
      state = 8;
      continue;
      state = 1;
    }
  }

  public void logic_game_refresh()
  {
    int i = 0;
    int j = 0;
    label4: int k;
    label24: int i3;
    label44: int l;
    if (j >= npc.length)
    {
      if (wave >= 100)
        break label156;
      k = wave;
      if (i >= Data.NPC_REFRESH_COUNT[k])
      {
        next_wave = true;
        refresh_time = 0;
        i3 = 0;
        if (i3 < npc.length)
          break label169;
      }
      if (next_wave)
        refresh_time = 1 + refresh_time;
      if (refresh_time >= 40)
      {
        next_wave = false;
        fresh_time = 1 + fresh_time;
        fresh_time %= Data.NPC_REFRESH_TIME[Data.NPC_ORDER[k]];
        if (fresh_time == 0)
          l = Data.NPC_REFRESH_COUNT[k];
      }
    }
    for (int i1 = 0; ; ++i1)
    {
      if (i1 >= l);
      while (true)
      {
        label126: return;
        if ((npc[j] == null) || (npc[j].state == 3))
          ++i;
        ++j;
        break label4:
        label156: k = 80 + wave % 20;
        break label24:
        label169: npc[i3].state = 0;
        ++i3;
        break label44:
        if (npc[i1].state != 0)
          break;
        if (i1 == 0)
          wave = 1 + wave;
        if (wave < 100);
        for (int i2 = wave; ; i2 = 80 + wave % 20)
        {
          kind = Data.NPC_ORDER[i2];
          switch (choice_map)
          {
          default:
            break;
          case 0:
            npc[i1].init(in_0[0], in_0[1], out_0[0], out_0[1], kind, 0, 0, i2);
            break label126:
          case 1:
          case 2:
          case 3:
          }
        }
        if (wave % 2 == 0)
          npc[i1].init(in_1[0], in_1[1], out_1[0], out_1[1], kind, 1, 0, i2);
        npc[i1].init(in_0[0], in_0[1], out_0[0], out_0[1], kind, 0, 0, i2);
        continue;
        if (wave % 2 == 0)
          npc[i1].init(in_1[0], in_1[1], out_1[0], out_1[1], kind, 1, 1, i2);
        npc[i1].init(in_0[0], in_0[1], out_0[0], out_0[1], kind, 0, 0, i2);
        continue;
        npc[i1].init(in_0[0], in_0[1], out_0[0], out_0[1], kind, 0, 0, i2);
      }
    }
  }

  public void logic_npc()
  {
    for (int i = 0; ; ++i)
    {
      if (i >= npc.length)
        return;
      if (npc[i] == null)
        continue;
      npc[i].logic();
    }
  }

  public Rect newRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return new Rect(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }

  public int num_length(int paramInt)
  {
    for (int i = 1; ; ++i)
    {
      if (paramInt < 10)
        return i;
      paramInt /= 10;
    }
  }

  public void off_xy()
  {
    if (off_x > 0)
    {
      off_x = 0;
      label10: if (off_y <= 0)
        break label47;
      off_y = 0;
    }
    while (true)
    {
      return;
      if (off_x < scr_w - 1080);
      off_x = scr_w - 1080;
      break label10:
      label47: if (off_y >= scr_h - 720)
        continue;
      off_y = scr_h - 720;
    }
  }

  public void onLowMemory()
  {
    outImg();
    switch (state)
    {
    case 6:
    case 7:
    case 10:
    default:
    case 5:
    case 1:
    case 2:
    case 3:
    case 4:
    case 8:
    case 9:
    case 11:
    case 0:
    }
    while (true)
    {
      return;
      initHightScore();
      initHightScoreImg();
      continue;
      initMainMenu();
      initMainMenuImg();
      continue;
      initSure();
      initSureImg();
      continue;
      initAbout();
      initAboutImg();
      continue;
      initHelp();
      initHelpImg();
      continue;
      initChoiceMap();
      initChoiceMapImg();
      continue;
      initChoiceMode();
      continue;
      initQuitImg();
      continue;
      initLogoImg();
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.pause);
    int i;
    int j;
    for (int k = 0; ; k = 1)
    {
      return k;
      will_music = false;
      this.is_exit = false;
      i = (int)paramMotionEvent.getX();
      j = (int)paramMotionEvent.getY();
      label64: switch (paramMotionEvent.getAction())
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    this.touchEvent = 0;
    this.touchX = i;
    this.touchY = j;
    this.prs_x = i;
    this.prs_y = j;
    this.beforeMoveX = i;
    this.beforeMoveY = j;
    int i4;
    label160: int i3;
    if (state == 7)
      if (!this.game_pause)
      {
        if (Tool.isin(i, j, startX, scr_h - bmpIconGooTower.getHeight(), bmpIconGooTower.getWidth() * towerCount, bmpIconGooTower.getHeight()))
        {
          i4 = 0;
          if (i4 < towerCount)
            break label369;
        }
        if (Tool.isin(i, j, scr_w - bm_faster.getWidth(), 20 + bmpHeart.getHeight(), bm_faster.getWidth(), bm_faster.getHeight()))
        {
          if (!is_faster)
            break label440;
          i3 = 0;
          label216: is_faster = i3;
          if (!is_playmusic)
            break label446;
        }
      }
    try
    {
      mp_music.prepare();
      mp_music.start();
      label239: tower.upDateOrSell(towers, i, j);
      if (Tool.isin(i, j, 5 + 2 * bm_pause.getWidth(), scr_h - bm_setting.getHeight() - 10, bm_setting.getWidth(), bm_setting.getHeight()))
        state = 11;
      if (Tool.isin(i, j, 10, scr_h - bm_pause.getHeight() - 10, 5 + bm_pause.getWidth(), 5 + bm_pause.getHeight()));
      if (this.game_pause);
      for (int i2 = 0; ; i2 = 1)
      {
        this.game_pause = i2;
        if (!this.game_pause)
          break;
        mp_music.pause();
        break label64:
        if ((Tool.isin(i, j, startX + i4 * bmpIconGooTower.getWidth(), scr_h - bmpIconGooTower.getHeight(), bmpIconGooTower.getWidth(), bmpIconGooTower.getHeight())) && (i4 + 1 <= this.canBuildNum))
        {
          label369: this.selectedTowerType = i4;
          this.isSelect = true;
        }
        ++i4;
        break label160:
        label440: i3 = 1;
        break label216:
        label446: mp_music.pause();
        break label239:
      }
      if (is_playmusic);
      mp_music = null;
      mp_music = tool.creatMediaPlayer(2130968583);
      label1410: label541: label1323: label584: label599: label496: mp_music.setLooping(true);
    }
    catch (Exception localException3)
    {
      int i1;
      try
      {
        mp_music.prepare();
        mp_music.start();
        break label64:
        if (state == 12)
          if (Tool.prsButton(i, j, 260, 80, 320, 135))
            if (is_playmusic)
            {
              i1 = 0;
              is_playmusic = i1;
              if (!is_playmusic)
                break label599;
              mp_music = tool.creatMediaPlayer(2130968583);
              mp_music.setLooping(true);
              if (this.game_pause);
            }
      }
      catch (Exception localException2)
      {
        try
        {
          mp_music.prepare();
          mp_music.start();
          break label64:
          i1 = 1;
          break label541:
          mp_music.pause();
          break label64:
          if (Tool.prsButton(i, j, 260, 135, 320, 190))
          {
            if (is_shake);
            for (int l = 0; ; l = 1)
            {
              is_shake = l;
              if (is_shake);
              vibrator.vibrate(80L);
              break label64:
            }
          }
          if (Tool.prsButton(i, j, 380, 0, 480, 80))
          {
            if (this.options_time == -1);
            this.options_time = 0;
            this.options_selected = 0;
          }
          if ((Tool.prsButton(i, j, 180, 190, 280, 260)) && (this.options_time == -1));
          this.options_time = 0;
          this.options_selected = 1;
          break label64:
          if (state == 1)
          {
            if (this.mainmenu_canSelect);
            if (Tool.prsButton(i, j, (scr_w >> 1) - 60, 104 + (scr_h >> 1), 60 + (scr_w >> 1), 132 + (scr_h >> 1)))
            {
              if (this.mainMenu_time == -1)
                this.mainMenu_time = 0;
              k = 1;
            }
            this.mainMenu_time = -1;
          }
          if (state == 2)
          {
            if (this.sureNewGame_time == -1);
            this.sureNewGame_time = 0;
            if (Tool.prsButton(i, j, 100, 180, 220, 240))
              this.sureNewGame_selected = 0;
            if (Tool.prsButton(i, j, 260, 180, 380, 240))
              this.sureNewGame_selected = 1;
            if (Tool.prsButton(i, j, 380, 0, 480, 80))
              this.sureNewGame_selected = 2;
            this.sureNewGame_time = -1;
          }
          if (state == 8)
          {
            this.choiceMap_time = 0;
            if (Tool.prsButton(i, j, 410, 25, 480, 65))
              this.choiceMap_selected = 4;
            if (Tool.prsButton(i, j, 60, 40, 240, 160))
              this.choiceMap_selected = 0;
            if (Tool.prsButton(i, j, 240, 40, 420, 160))
            {
              if (!is_mapDust_open)
              {
                if (this.toast != null)
                  this.toast.cancel();
                this.toast.setText("地图未解锁");
                this.toast.show();
                this.choiceMap_time = -1;
              }
              this.choiceMap_selected = 1;
            }
            if (Tool.prsButton(i, j, 60, 160, 240, 280))
            {
              if (!is_mapForest_open)
              {
                if (this.toast != null)
                  this.toast.cancel();
                this.toast.setText("地图未解锁");
                this.toast.show();
                this.choiceMap_time = -1;
              }
              this.choiceMap_selected = 2;
            }
            if (Tool.prsButton(i, j, 240, 160, 420, 280))
            {
              if (!is_mapHill_open)
              {
                if (this.toast != null)
                  this.toast.cancel();
                this.toast.setText("地图未解锁");
                this.toast.show();
                this.choiceMap_time = -1;
              }
              this.choiceMap_selected = 3;
            }
            this.choiceMap_time = -1;
          }
          if (state == 9)
          {
            this.choiceMode_time = 0;
            if (Tool.prsButton(i, j, 80, 255, 205, 305))
              this.choiceMode_selected = 0;
            if (Tool.prsButton(i, j, 390, 25, 430, 60))
              this.choiceMode_selected = 1;
            if (Tool.prsButton(i, j, 60, 210, 120, 245))
            {
              difficulty = 0;
              if (!Tool.prsButton(i, j, 300, 170, 400, 195))
                break label1410;
              mode = 0;
            }
            while (true)
            {
              this.choiceMode_time = -1;
              break label64:
              if (Tool.prsButton(i, j, 140, 210, 195, 245))
                difficulty = 1;
              if (Tool.prsButton(i, j, 220, 210, 280, 245));
              difficulty = 2;
              break label1323:
              if (Tool.prsButton(i, j, 300, 195, 400, 223))
              {
                switch (choice_map)
                {
                default:
                  break;
                case 0:
                  if (!is_mapICC_unlimit_open)
                  {
                    this.toast.setText("模式未解锁");
                    this.toast.show();
                    this.choiceMode_time = -1;
                  }
                  mode = 1;
                  break;
                case 1:
                  if (!is_mapDust_unlimit_open)
                  {
                    this.toast.setText("模式未解锁");
                    this.toast.show();
                    this.choiceMode_time = -1;
                  }
                  mode = 1;
                  break;
                case 2:
                  if (!is_mapForest_unlimit_open)
                  {
                    this.toast.setText("模式未解锁");
                    this.toast.show();
                    this.choiceMode_time = -1;
                  }
                  mode = 1;
                  break;
                case 3:
                }
                if (!is_mapHill_unlimit_open)
                {
                  this.toast.setText("模式未解锁");
                  this.toast.show();
                  this.choiceMode_time = -1;
                }
                mode = 1;
              }
              if (!Tool.prsButton(i, j, 300, 223, 400, 250))
                continue;
              switch (choice_map)
              {
              default:
                break;
              case 0:
                if (!is_mapICC_unlimit_open)
                {
                  this.toast.setText("模式未解锁");
                  this.toast.show();
                  this.choiceMode_time = -1;
                }
                mode = 2;
                break;
              case 1:
                if (!is_mapDust_unlimit_open)
                {
                  this.toast.setText("模式未解锁");
                  this.toast.show();
                  this.choiceMode_time = -1;
                }
                mode = 2;
                break;
              case 2:
                if (!is_mapForest_unlimit_open)
                {
                  this.toast.setText("模式未解锁");
                  this.toast.show();
                  this.choiceMode_time = -1;
                }
                mode = 2;
                break;
              case 3:
              }
              if (!is_mapHill_unlimit_open)
              {
                this.toast.setText("模式未解锁");
                this.toast.show();
                this.choiceMode_time = -1;
              }
              mode = 2;
            }
          }
          if (state == 11)
          {
            this.quit_time = 0;
            if (Tool.prsButton(i, j, 410, 0, 480, 65))
              this.quit_selected = 0;
            if (Tool.prsButton(i, j, 110, 60, 380, 120))
              this.quit_selected = 1;
            if (Tool.prsButton(i, j, 110, 120, 380, 165))
              this.quit_selected = 2;
            if (Tool.prsButton(i, j, 110, 165, 380, 210))
              this.quit_selected = 3;
            if (Tool.prsButton(i, j, 110, 210, 380, 260))
              this.quit_selected = 4;
            this.quit_time = -1;
          }
          if (state == 3)
          {
            this.about_time = 0;
            if (Tool.prsButton(i, j, 400, 0, 480, 80))
              this.about_select = 0;
            this.about_time = -1;
          }
          if (state == 4)
          {
            this.help_time = 0;
            if (Tool.prsButton(i, j, 50, 150, 110, 200))
              this.help_selected = 0;
            if (Tool.prsButton(i, j, 380, 150, 460, 200))
              this.help_selected = 1;
            if (Tool.prsButton(i, j, 400, 0, 480, 70))
              this.help_selected = 2;
            this.help_time = -1;
          }
          if (state == 5);
          this.high_time = 0;
          if (Tool.prsButton(i, j, 410, 0, 480, 60))
            this.high_selected = 0;
          this.high_time = -1;
          if (Tool.prsButton(i, j, 40, 50, 120, 110))
            this.highscore_choice_map = 0;
          if (Tool.prsButton(i, j, 40, 110, 120, 165))
            this.highscore_choice_map = 1;
          if (Tool.prsButton(i, j, 40, 165, 120, 215))
            this.highscore_choice_map = 2;
          if (Tool.prsButton(i, j, 40, 215, 120, 270));
          this.highscore_choice_map = 3;
          break label64:
          this.touchEvent = 1;
          this.touchX = i;
          this.touchY = j;
          if (state == 7)
            if (!this.game_pause)
            {
              if (((j > scr_h - bmpIconGooTower.getHeight()) && (i < startX)) || ((j < scr_h - bmpIconGooTower.getHeight()) && (!Tool.isin(i, j, scr_w - bm_faster.getWidth(), 20 + bmpHeart.getHeight(), bm_faster.getWidth(), bm_faster.getHeight())) && (!Tool.isin(i, j, 20, scr_h - bm_pause.getHeight() - 10, bm_pause.getWidth(), bm_pause.getHeight())) && (!Tool.isin(i, j, 5 + 2 * bm_pause.getWidth(), scr_h - bm_setting.getHeight() - 10, bm_setting.getWidth(), bm_setting.getHeight())) && (!this.isReallyMove) && (this.isShowUpdate) && (Tower.checkUpdateOrSell(towers, i, j))))
                this.isShowUpdate = false;
              if (!this.isShowUpdate)
              {
                this.con = (1 + this.con);
                if (this.con == 2)
                {
                  this.isShowUpdate = true;
                  this.con = 0;
                }
              }
              if ((Tool.isin(i, j, startX, scr_h - bmpIconGooTower.getHeight(), bmpIconGooTower.getWidth() * towerCount, bmpIconGooTower.getHeight())) && (!this.isOutOfSelectArea))
                this.isSelect = false;
              if ((this.isOutOfSelectArea) && (this.isSelect))
              {
                if ((tower.isCanBuild()) && (check_can_build_in_path(Tower.getNowRow(j) - 1, Tower.getNowCol(i), true)))
                {
                  Tower.buildTower(Tower.getNowRow(j) - 1, Tower.getNowCol(i), this.selectedTowerType);
                  buildTower(tower.getX(i), tower.getY(j));
                }
                this.isOutOfSelectArea = false;
                this.isSelect = false;
              }
            }
          while (true)
          {
            this.isReallyMove = false;
            break label64:
            if (state == 1)
            {
              if ((Math.abs(50 + (this.menuz_x - 240)) % 100 > 95) || (Math.abs(50 + (this.menuz_x - 240)) % 100 < 5))
                k = 0;
              if (Math.abs(50 + (this.menuz_x - 240)) % 100 <= 50)
                this.menu_move_right = true;
              if (Math.abs(50 + (this.menuz_x - 240)) % 100 <= 50)
                continue;
              this.menu_move_left = true;
            }
            if ((state == 2) || (state == 8) || (state == 9))
              continue;
          }
          this.touchEvent = 2;
          this.touchX = i;
          this.touchY = j;
          this.dis_x = Math.abs(i - this.beforeMoveX);
          this.dis_y = Math.abs(j - this.beforeMoveY);
          if ((this.dis_x > 20) || (this.dis_y > 20))
            this.isReallyMove = true;
          if (state == 1);
          this.menuz_x += i - this.prs_x;
          this.prs_x = i;
          if (this.menuz_x > 190)
            this.menuz_x = 190;
          if (this.menuz_x < -410);
          this.menuz_x = -410;
        }
        catch (Exception localException1)
        {
          break label584:
          localException2 = localException2;
        }
        break label496:
        localException3 = localException3;
      }
    }
  }

  public void outGame()
  {
    cleanNpcAndTower();
    cleanImg();
    System.gc();
  }

  public void outImg()
  {
    this.bm_logos[0] = null;
    this.bm_logos[1] = null;
    bm_main = null;
    this.bm_menu_z = null;
    this.bm_sure = null;
    this.bm_choice_map = null;
    this.bm_about = null;
    this.bm_help_image = null;
    this.bm_help = null;
    this.bm_hight_score = null;
    this.bm_hight_score = null;
    System.gc();
  }

  public void paint(Canvas paramCanvas)
  {
    paramCanvas.drawColor(-1);
    switch (state)
    {
    case 6:
    case 10:
    default:
    case 0:
    case 1:
    case 2:
    case 8:
    case 9:
    case 3:
    case 4:
    case 5:
    case 11:
    case 12:
    case 7:
    }
    while (true)
    {
      return;
      drawLogo(paramCanvas);
      continue;
      drawMainMenu(paramCanvas);
      continue;
      drawSure(paramCanvas);
      continue;
      drawChoiceMap(paramCanvas);
      continue;
      drawChoiceMode(paramCanvas);
      continue;
      drawAbout(paramCanvas);
      continue;
      drawHelp(paramCanvas);
      continue;
      drawHighScore(paramCanvas);
      continue;
      drawQuit(paramCanvas);
      continue;
      drawOptions(paramCanvas);
      continue;
      drawGame(paramCanvas);
    }
  }

  public void pointerDragred(int paramInt1, int paramInt2)
  {
    if (state == 7)
    {
      if (this.isSelect)
        break label56;
      off_x += paramInt1 - this.prs_x;
      off_y += paramInt2 - this.prs_y;
      off_xy();
      this.prs_x = paramInt1;
      this.prs_y = paramInt2;
    }
    label55: label56: 
    do
      return;
    while (!this.isReallyMove);
    this.isOutOfSelectArea = true;
    if ((this.pre[0] != Tower.getNowRow(paramInt2) - 1) || (this.pre[1] != Tower.getNowCol(paramInt1)))
    {
      this.pre[0] = (Tower.getNowRow(paramInt2) - 1);
      this.pre[1] = Tower.getNowCol(paramInt1);
      this.will_check_path = true;
    }
    while (true)
    {
      tower.checkBuild(paramInt1, paramInt2, this.canBuild);
      this.move_tower_x += 48 * ((paramInt1 - this.move_tower_x) / 48);
      this.move_tower_y += 48 * ((paramInt2 - this.move_tower_y) / 48);
      this.move_tower_x = paramInt1;
      this.move_tower_y = paramInt2;
      break label55:
      if (!this.will_check_path)
        continue;
      this.will_check_path = false;
      this.pre[0] = (Tower.getNowRow(paramInt2) - 1);
      this.pre[1] = Tower.getNowCol(paramInt1);
      this.canBuild = check_can_build_in_path(this.pre[0], this.pre[1], false);
    }
  }

  public void pointerPressed(int paramInt1, int paramInt2)
  {
  }

  public void pointerRaleased(int paramInt1, int paramInt2)
  {
    switch (state)
    {
    default:
    case 7:
    }
    while (true)
    {
      return;
      if ((Tool.isin(paramInt1, paramInt2, startX, scr_h - bmpIconGooTower.getHeight(), bmpIconGooTower.getWidth() * towerCount, bmpIconGooTower.getHeight())) && (!this.isOutOfSelectArea))
        this.isSelect = false;
      if ((!this.isOutOfSelectArea) || (!this.isSelect))
        continue;
      if ((tower.isCanBuild()) && (check_can_build_in_path(Tower.getNowRow(paramInt2) - 1, Tower.getNowCol(paramInt1), true)))
      {
        Tower.buildTower(Tower.getNowRow(paramInt2) - 1, Tower.getNowCol(paramInt1), this.selectedTowerType);
        buildTower(tower.getX(paramInt1), tower.getY(paramInt2));
      }
      this.isOutOfSelectArea = false;
      this.isSelect = false;
    }
  }

  public GameIo readData(String paramString1, String paramString2)
  {
    GameIo localGameIo1 = null;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(MainActivity.mainActivity.getSharedPreferences(paramString1, 0).getString(paramString2, "").getBytes()));
    GameIo localGameIo2;
    try
    {
      ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
      localGameIo1 = (GameIo)localObjectInputStream.readObject();
      localObjectInputStream.close();
      localGameIo2 = localGameIo1;
      return localGameIo2;
    }
    catch (Exception localException)
    {
      localGameIo2 = localGameIo1;
    }
  }

  public IsFirstRun readIsFirst(String paramString1, String paramString2)
  {
    IsFirstRun localIsFirstRun1 = null;
    SharedPreferences localSharedPreferences = MainActivity.mainActivity.getSharedPreferences(paramString1, 0);
    ByteArrayInputStream localByteArrayInputStream;
    try
    {
      String str = localSharedPreferences.getString(paramString2, "");
      localByteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(str.getBytes()));
    }
    catch (Exception localException1)
    {
      IsFirstRun localIsFirstRun2;
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
        localIsFirstRun1 = (IsFirstRun)localObjectInputStream.readObject();
        localObjectInputStream.close();
        localIsFirstRun2 = localIsFirstRun1;
        return localIsFirstRun2;
        localException1 = localException1;
        localIsFirstRun2 = null;
      }
      catch (Exception localException2)
      {
        localIsFirstRun2 = localIsFirstRun1;
      }
    }
  }

  public GameMenuIo readMenu(String paramString1, String paramString2)
  {
    GameMenuIo localGameMenuIo1 = null;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(MainActivity.mainActivity.getSharedPreferences(paramString1, 0).getString(paramString2, "").getBytes()));
    GameMenuIo localGameMenuIo2;
    try
    {
      ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
      localGameMenuIo1 = (GameMenuIo)localObjectInputStream.readObject();
      localObjectInputStream.close();
      localGameMenuIo2 = localGameMenuIo1;
      return localGameMenuIo2;
    }
    catch (Exception localException)
    {
      localGameMenuIo2 = localGameMenuIo1;
    }
  }

  public GameScoreIo readScore(String paramString1, String paramString2)
  {
    GameScoreIo localGameScoreIo1 = null;
    SharedPreferences localSharedPreferences = MainActivity.mainActivity.getSharedPreferences(paramString1, 0);
    ByteArrayInputStream localByteArrayInputStream;
    try
    {
      String str = localSharedPreferences.getString(paramString2, "");
      localByteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(str.getBytes()));
    }
    catch (Exception localException1)
    {
      GameScoreIo localGameScoreIo2;
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
        localGameScoreIo1 = (GameScoreIo)localObjectInputStream.readObject();
        localObjectInputStream.close();
        localGameScoreIo2 = localGameScoreIo1;
        return localGameScoreIo2;
        localException1 = localException1;
        localGameScoreIo2 = null;
      }
      catch (Exception localException2)
      {
        localGameScoreIo2 = localGameScoreIo1;
      }
    }
  }

  public void resume()
  {
    this.pause = false;
    this.game_pause = true;
  }

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: invokestatic 1791	android/os/SystemClock:currentThreadTimeMillis	()J
    //   3: lstore_1
    //   4: aload_0
    //   5: getfield 408	com/anjoystudio/pandoradef/GameView:thread_run	Z
    //   8: ifne +85 -> 93
    //   11: return
    //   12: aconst_null
    //   13: astore_3
    //   14: aload_0
    //   15: invokevirtual 1792	com/anjoystudio/pandoradef/GameView:logic	()V
    //   18: aload_0
    //   19: getfield 414	com/anjoystudio/pandoradef/GameView:surface_holder	Landroid/view/SurfaceHolder;
    //   22: invokeinterface 1796 1 0
    //   27: astore_3
    //   28: aload_0
    //   29: aload_3
    //   30: invokevirtual 1798	com/anjoystudio/pandoradef/GameView:paint	(Landroid/graphics/Canvas;)V
    //   33: ldc2_w 1799
    //   36: invokestatic 1791	android/os/SystemClock:currentThreadTimeMillis	()J
    //   39: lload_1
    //   40: lsub
    //   41: lsub
    //   42: lstore 8
    //   44: lload 8
    //   46: lconst_0
    //   47: lcmp
    //   48: ifle +55 -> 103
    //   51: getstatic 759	com/anjoystudio/pandoradef/GameView:is_faster	Z
    //   54: ifeq +16 -> 70
    //   57: lload 8
    //   59: iconst_2
    //   60: lshr
    //   61: lstore 8
    //   63: lload 8
    //   65: lconst_0
    //   66: lcmp
    //   67: ifle +42 -> 109
    //   70: lload 8
    //   72: invokestatic 1803	android/os/SystemClock:sleep	(J)V
    //   75: aload_3
    //   76: ifnull +17 -> 93
    //   79: aload_0
    //   80: getfield 414	com/anjoystudio/pandoradef/GameView:surface_holder	Landroid/view/SurfaceHolder;
    //   83: aload_3
    //   84: invokeinterface 1806 2 0
    //   89: invokestatic 1791	android/os/SystemClock:currentThreadTimeMillis	()J
    //   92: lstore_1
    //   93: aload_0
    //   94: getfield 942	com/anjoystudio/pandoradef/GameView:pause	Z
    //   97: ifeq -85 -> 12
    //   100: goto -96 -> 4
    //   103: lconst_0
    //   104: lstore 8
    //   106: goto -55 -> 51
    //   109: lconst_0
    //   110: lstore 8
    //   112: goto -42 -> 70
    //   115: astore 7
    //   117: aload 7
    //   119: invokevirtual 1079	java/lang/Exception:printStackTrace	()V
    //   122: aload_3
    //   123: ifnull -30 -> 93
    //   126: aload_0
    //   127: getfield 414	com/anjoystudio/pandoradef/GameView:surface_holder	Landroid/view/SurfaceHolder;
    //   130: aload_3
    //   131: invokeinterface 1806 2 0
    //   136: invokestatic 1791	android/os/SystemClock:currentThreadTimeMillis	()J
    //   139: lstore_1
    //   140: goto -47 -> 93
    //   143: astore 4
    //   145: aload_3
    //   146: ifnull +17 -> 163
    //   149: aload_0
    //   150: getfield 414	com/anjoystudio/pandoradef/GameView:surface_holder	Landroid/view/SurfaceHolder;
    //   153: aload_3
    //   154: invokeinterface 1806 2 0
    //   159: invokestatic 1791	android/os/SystemClock:currentThreadTimeMillis	()J
    //   162: pop2
    //   163: aload 4
    //   165: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   14	75	115	java/lang/Exception
    //   14	75	143	finally
    //   117	122	143	finally
  }

  public boolean save()
  {
    this.gi = new GameIo(MainActivity.mainActivity.gv);
    Tool.saveData("pandoraDefence", "save", this.gi);
    return true;
  }

  public void saveMenu()
  {
    Tool.saveData("pandoraDefence", "menu", new GameMenuIo(this));
  }

  public void saveScore()
  {
    for (int i = this.hiscoMemory.length - 1; ; --i)
    {
      if (i < 0)
        Tool.saveData("pandoraDefence", "score", new GameScoreIo(this));
      do
      {
        return;
        if (this.hiscoMemory[i].map != choice_map)
          break label112;
      }
      while (score <= this.hiscoMemory[i].score);
      this.hiscoMemory[i] = new HightScoreMemory(choice_map, score, wave);
      HightScoreMemory[] arrayOfHightScoreMemory = sortMemory();
      for (int j = 0; ; ++j)
      {
        if (j <= 5);
        label112: this.hiscoMemory[(j + (i - 5))] = arrayOfHightScoreMemory[j];
      }
    }
  }

  public void save_isFirstRun(int paramInt)
  {
    Tool.saveData("pandoraDefence", "is_first_run", new IsFirstRun(paramInt));
  }

  public AlertDialog.Builder showDialog()
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(MainActivity.mainActivity);
    CharSequence[] arrayOfCharSequence = this.items;
    boolean[] arrayOfBoolean = new boolean[2];
    arrayOfBoolean[0] = is_playmusic;
    arrayOfBoolean[1] = is_shake;
    AlertDialog.Builder localBuilder2 = localBuilder1.setMultiChoiceItems(arrayOfCharSequence, arrayOfBoolean, new GameView.1(this)).setPositiveButton("确定", new GameView.2(this));
    localBuilder2.create().show();
    return localBuilder2;
  }

  public HightScoreMemory[] sortMemory()
  {
    Vector localVector = new Vector();
    int i = 0;
    label10: HightScoreMemory[] arrayOfHightScoreMemory1;
    int j;
    HightScoreMemory[] arrayOfHightScoreMemory2;
    if (i >= this.hiscoMemory.length)
    {
      arrayOfHightScoreMemory1 = new HightScoreMemory[6];
      j = 0;
      if (j < arrayOfHightScoreMemory1.length)
        break label94;
      arrayOfHightScoreMemory2 = Tool.Sort(arrayOfHightScoreMemory1, 0, arrayOfHightScoreMemory1.length - 1);
    }
    for (int k = 0; ; ++k)
    {
      if (k >= 3)
      {
        return arrayOfHightScoreMemory2;
        if (this.hiscoMemory[i].map == choice_map)
          localVector.add(this.hiscoMemory[i]);
        ++i;
        break label10:
        label94: arrayOfHightScoreMemory1[j] = ((HightScoreMemory)localVector.elementAt(j));
        ++j;
      }
      HightScoreMemory localHightScoreMemory = arrayOfHightScoreMemory2[k];
      arrayOfHightScoreMemory2[k] = arrayOfHightScoreMemory2[(5 - k)];
      arrayOfHightScoreMemory2[(5 - k)] = localHightScoreMemory;
    }
  }

  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (!is_first_run)
      return;
    scr_w = getWidth();
    scr_h = getHeight();
    off_y = scr_h - 720 >> 1;
    if (state == 0)
      initLogo();
    game_thread.start();
    is_first_run = false;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
  }

  public void touchEvent()
  {
    switch (this.touchEvent)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return;
      pointerPressed(this.touchX, this.touchY);
      continue;
      pointerRaleased(this.touchX, this.touchY);
      continue;
      pointerDragred(this.touchX, this.touchY);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.GameView
 * JD-Core Version:    0.5.4
 */