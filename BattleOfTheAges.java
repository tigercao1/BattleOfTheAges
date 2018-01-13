/*********************************************************************************************
'BATTLES OF THE AGES'
BY EDMOND LU & TIGER CAO
2014-2015
MRS. MEGSON
ICS3U0
SUMMATIVE 2014-2015

This applet is for a game; Battle of the Ages (BotA). Battle of the Ages is heavily based on
flash game 'Age of War 2', a flash game created by Louissi. The game's main objective is to
destroy the enemy base before they destroy yours. This is achieved by spawning units,
evolving ages and the special ability (not implemented). Killing units garners a specific
amount of gold, which can be used to by more units in turn. Enemies spawn at algorithmic
intervals, implying a cooldown inbetween each spawn; the stronger the unit - the higher the
cooldown. A rectangular hitbox system allows units to automatically collide and change their
state, varying from attacking, to stationary and walking. The game also includes a music clip
(sourced).

Resources and Sources from the program:
The main music clip: https://www.youtube.com/watch?v=alMZ3R1FsGM
Idea of game: 'Age of War' by Louissi
All implemented methods and examples were learnt from: docs.oracle.com/
Images of units and icons were taken from 'Age of War'
*********************************************************************************************/
//Imported classes:
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.Graphics;
import java.lang.Object;
import java.awt.Rectangle;

public class BattleOfTheAges extends Applet implements Runnable, ActionListener, MouseListener,
    MouseMotionListener
{
    // Initialization of variables
    int x_speed; //speed at which any which unit moves
    int gold;    //players gold
    int largest; //variable used to find playerLead
    int smallest;  //variable used to find enemyLead
    int playerLead;  //playerLead is the first in line for player units
    int enemyLead; //enemyLead is the first in line for enemy units
    int numberOfPUnits;  //total number of player units
    int numberOfEUnits;  //total number of enemy units
    int totalUnits;    //total number of all units
    int playerPop;    //player unit current population on screen
    int enemyPop;    //enemy units current population on screen
    int showText;    //will show different text depending on value
    int[] playerState = new int [1000];  //an array of int: players status
    int[] enemyState = new int [1000];   //an array of int: enemies status
    int[] typeOfUnit = new int [1000];  //an array of int: what type of unit each unit is
    int[] enemyTypeOfUnit = new int [1000];  //an array of int: what type of enemy unit each unit is
    int[] playerXPos = new int [1000]; //an array of int: player unit x position
    int[] enemyXPos = new int [1000]; //an array of int: enemy unit x position
    int[] playerHP = new int [1000]; //an array of int: the health points of a unit
    int[] enemyHP = new int [1000]; //an array of int: the health points of an enemy unit
    int[] playerDMG = new int [1000]; //an array of int: the damage that each unit deals
    int[] enemyDMG = new int [1000]; //an array of int: the damage that each enemy unit deals
    Font font1 = new Font ("Algerian", Font.PLAIN, 20);  //declare font algerian, plain, size 20
    Font font2 = new Font ("Candara", Font.PLAIN, 12);  //declare font candara, plain, size 12
    int exp;   //the players experience points ,used to evolve ages
    int enemySpawn;  //an integer to be randomly generated, will determine the type of unit the enemy will spawn
    int era;  //current era , total 2
    int gameState;  //gameState (1=title, 2=ingame, 3=instructions, 4=defeat, 5= victory)
    boolean notEnoughExp;  //true when player doesnt have enough exp to evolve
    boolean notEnoughGold;  //true when player doesnt have enough gold to buy a unit
    boolean spawnUnit;   //which unit will be spawned when icon clicked
    int walkCount;  //walk counter, counts once every tick; 60 ticks per second, resets to 0 when it reaches 60
    long cooldownCount; //cooldown counter, counts once every tick
    long playerCD; //player cooldown
    long enemyCD; //enemy cooldown
    int lastEnemyCD; //enemy last cooldown

    int width1, width2;

    int[] playerRectX = new int [1000];  //player hitbox rectangle position
    int[] enemyRectX = new int [1000]; //enemy hitbox rectangle position

    int mx, my; //mouse x and y positions

    boolean isMousePressed;   //is left mouse button clicked down

    Random ranNum = new Random (); //create generator

    // declare two instance variables at the head of the program
    private Image dbImage;
    private Graphics dbg;

    //declare pictures
    //declare stoneage images; unit and enemy
    private final String SM1_PATH = "SM1.png"; //Stone Melee
    private final String ESM1_PATH = "ESM1.png";
    private final String SM2_PATH = "SM2.png";
    private final String ESM2_PATH = "ESM2.png";
    private final String SM3_PATH = "SM3.png";
    private final String ESM3_PATH = "ESM3.png";
    private final String SR1_PATH = "SR1.png"; //Stone Ranged
    private final String ESR1_PATH = "ESR1.png";
    private final String SR2_PATH = "SR2.png";
    private final String ESR2_PATH = "ESR2.png";
    private final String SR3_PATH = "SR3.png";
    private final String ESR3_PATH = "ESR3.png";
    private final String ST1_PATH = "ST1.png"; //Stone Tank
    private final String EST1_PATH = "EST1.png";
    private final String ST2_PATH = "ST2.png";
    private final String EST2_PATH = "EST2.png";
    private final String SMA1_PATH = "SMA1.png"; //stone melee attack
    private final String ESMA1_PATH = "ESMA1.png";
    private final String SMA2_PATH = "SMA2.png";
    private final String ESMA2_PATH = "ESMA2.png";
    private final String SRA_PATH = "SRA.png"; //stone ranged attack
    private final String ESRA_PATH = "ESRA.png";
    private final String STA_PATH = "STA.png"; //stone tank attack
    private final String ESTA_PATH = "ESTA.png";

    //declare medieval images; unit and enemy
    private final String MDM1_PATH = "MDM1.png"; //Medieval Melee
    private final String EMDM1_PATH = "EMDM1.png";
    private final String MDM2_PATH = "MDM2.png";
    private final String EMDM2_PATH = "EMDM2.png";
    private final String MDR1_PATH = "MDR1.png"; //Medieval Ranged
    private final String EMDR1_PATH = "EMDR1.png";
    private final String MDR2_PATH = "MDR2.png";
    private final String EMDR2_PATH = "EMDR2.png";
    private final String MDT1_PATH = "MDT1.png"; //Medieval Tank
    private final String EMDT1_PATH = "EMDT1.png";
    private final String MDT2_PATH = "MDT2.png";
    private final String EMDT2_PATH = "EMDT2.png";
    private final String MDMA1_PATH = "MDMA1.png"; //medieval melee attack
    private final String EMDMA1_PATH = "EMDMA1.png";
    private final String MDMA2_PATH = "MDMA2.png";
    private final String EMDMA2_PATH = "EMDMA2.png";
    private final String MDRA_PATH = "MDRA.png"; //medieval ranged attack
    private final String EMDRA_PATH = "EMDRA.png";
    private final String MDTA_PATH = "MDTA.png"; //medieval tank attack
    private final String EMDTA_PATH = "EMDTA.png";

    //declare the background of the main menu
    private final String Back_PATH = "Back.png";
    //declare instructions picture
    private final String Instruction_PATH = "Instruction.png";
    //declare textbox of units
    private final String textbox_PATH = "textbox.png";
    //declare background1
    private final String Background_PATH = "Background.png";
    //declare backgorund 2
    private final String Background2_PATH = "Background2.png";

    //declare all unit image names
    Image SM1, SM2, SM3, SR1, SR2, SR3, ST1, ST2, MDM1, MDM2, MDR1, MDR2, MDT1, MDT2, MM1, MM2, MR1, MR2, MT,
	ESM1, ESM2, ESM3, ESR1, ESR2, ESR3, EST1, EST2, EMDM1, EMDM2, EMDR1, EMDR2, EMDT1, EMDT2, EMM1, EMM2, EMR1, EMR2, EMT,
	SMA1, SMA2, SRA, STA, MDMA1, MDMA2, MDRA, MDTA, MMA, MTA, MRA, ESMA1, ESMA2, ESRA, ESTA, EMDMA1, EMDMA2, EMDRA, EMDTA, EMMA, EMTA, EMRA;
    //declare all background and text image names
    Image Background, Background2, Background3, textbox, Back, Instruction;
    int picWidth, picHeight;
    //declare music clip
    AudioClip mainMusic;

    public void init ()
    {
	// Place the body of the initialization method here
	gold = 275;  //set gold 275
	exp = 0;
	era = 1;  //start era
	largest = 0;
	smallest = 0;
	playerLead = 0;
	enemyLead = 0;
	numberOfPUnits = 0;
	numberOfEUnits = 0;
	totalUnits = 0;
	playerPop = 0;
	enemyPop = 0;
	walkCount = 0;
	showText = 0;
	playerCD = -140; //playercooldown is negative so that units cant be spawned instantly
	enemyCD = 0;
	enemyRectX [500] = 1450; //enemyRectX is the tower unit, set its hitbox at 1450
	playerHP [500] = 1000;  //playerHP of tower is 1000
	enemyHP [500] = 1000; //hp of enemy tower 1000
	cooldownCount = 15;
	notEnoughExp = false;
	gameState = 1;    //gamestate starts at 1 (title)
	notEnoughGold = false;
	isMousePressed = false;
	spawnUnit = false;
	resize (772, 616);  //size of window
	//mouse listener initialization
	addMouseListener (this);
	addMouseMotionListener (this);

	//get paths of stone age player images
	SM1 = getToolkit ().getImage (SM1_PATH);
	prepareImage (SM1, this);
	SM2 = getToolkit ().getImage (SM2_PATH);
	prepareImage (SM2, this);
	SM3 = getToolkit ().getImage (SM3_PATH);
	prepareImage (SM3, this);
	SR1 = getToolkit ().getImage (SR1_PATH);
	prepareImage (SR1, this);
	SR2 = getToolkit ().getImage (SR2_PATH);
	prepareImage (SR2, this);
	SR3 = getToolkit ().getImage (SR3_PATH);
	prepareImage (SR3, this);
	ST1 = getToolkit ().getImage (ST1_PATH);
	prepareImage (ST1, this);
	ST2 = getToolkit ().getImage (ST2_PATH);
	prepareImage (ST2, this);
	SMA1 = getToolkit ().getImage (SMA1_PATH);
	prepareImage (SMA1, this);
	SMA2 = getToolkit ().getImage (SMA2_PATH);
	prepareImage (SMA2, this);
	SRA = getToolkit ().getImage (SRA_PATH);
	prepareImage (ST2, this);
	STA = getToolkit ().getImage (STA_PATH);
	prepareImage (STA, this);

	//get paths of stone age enemy images
	ESM1 = getToolkit ().getImage (ESM1_PATH);
	prepareImage (ESM1, this);
	ESM2 = getToolkit ().getImage (ESM2_PATH);
	prepareImage (ESM2, this);
	ESM3 = getToolkit ().getImage (ESM3_PATH);
	prepareImage (ESM3, this);
	ESR1 = getToolkit ().getImage (ESR1_PATH);
	prepareImage (ESR1, this);
	ESR2 = getToolkit ().getImage (ESR2_PATH);
	prepareImage (ESR2, this);
	ESR3 = getToolkit ().getImage (ESR3_PATH);
	prepareImage (ESR3, this);
	EST1 = getToolkit ().getImage (EST1_PATH);
	prepareImage (EST1, this);
	EST2 = getToolkit ().getImage (EST2_PATH);
	prepareImage (EST2, this);
	ESMA1 = getToolkit ().getImage (ESMA1_PATH);
	prepareImage (ESMA1, this);
	ESMA2 = getToolkit ().getImage (ESMA2_PATH);
	prepareImage (ESMA2, this);
	ESRA = getToolkit ().getImage (ESRA_PATH);
	prepareImage (ESRA, this);
	ESTA = getToolkit ().getImage (ESTA_PATH);
	prepareImage (ESTA, this);

	//get paths of medieval player images
	MDM1 = getToolkit ().getImage (MDM1_PATH);
	prepareImage (MDM1, this);
	MDM2 = getToolkit ().getImage (MDM2_PATH);
	prepareImage (MDM1, this);
	MDR1 = getToolkit ().getImage (MDR1_PATH);
	prepareImage (MDR1, this);
	MDR2 = getToolkit ().getImage (MDR2_PATH);
	prepareImage (MDR1, this);
	MDT1 = getToolkit ().getImage (MDT1_PATH);
	prepareImage (MDT1, this);
	MDT2 = getToolkit ().getImage (MDT2_PATH);
	prepareImage (MDT1, this);
	MDMA1 = getToolkit ().getImage (MDMA1_PATH);
	prepareImage (MDMA1, this);
	MDMA2 = getToolkit ().getImage (MDMA2_PATH);
	prepareImage (MDMA2, this);
	MDRA = getToolkit ().getImage (MDRA_PATH);
	prepareImage (MDRA, this);
	MDTA = getToolkit ().getImage (MDTA_PATH);
	prepareImage (MDTA, this);

	//get paths of medieval enemy images
	EMDM1 = getToolkit ().getImage (EMDM1_PATH);
	prepareImage (EMDM1, this);
	EMDM2 = getToolkit ().getImage (EMDM2_PATH);
	prepareImage (EMDM1, this);
	EMDR1 = getToolkit ().getImage (EMDR1_PATH);
	prepareImage (EMDR1, this);
	EMDR2 = getToolkit ().getImage (EMDR2_PATH);
	prepareImage (EMDR1, this);
	EMDT1 = getToolkit ().getImage (EMDT1_PATH);
	prepareImage (EMDT1, this);
	EMDT2 = getToolkit ().getImage (EMDT2_PATH);
	prepareImage (EMDT1, this);
	EMDMA1 = getToolkit ().getImage (EMDMA1_PATH);
	prepareImage (EMDMA1, this);
	EMDMA2 = getToolkit ().getImage (EMDMA2_PATH);
	prepareImage (EMDMA2, this);
	EMDRA = getToolkit ().getImage (EMDRA_PATH);
	prepareImage (EMDRA, this);
	EMDTA = getToolkit ().getImage (EMDTA_PATH);
	prepareImage (EMDTA, this);

	//get path of background image
	Back = getToolkit ().getImage (Back_PATH);
	prepareImage (Back, this);
	//get path of instruction image
	Instruction = getToolkit ().getImage (Instruction_PATH);
	prepareImage (Instruction, this);
	//get path of textbox image
	textbox = getToolkit ().getImage (textbox_PATH);
	prepareImage (textbox, this);
	//get paths of the backgrounds
	Background = getToolkit ().getImage (Background_PATH);
	prepareImage (Background, this);
	Background2 = getToolkit ().getImage (Background2_PATH);
	prepareImage (Background2, this);

	//declare audio clip and retrive its folder path
	mainMusic = getAudioClip (getDocumentBase (), "mainMusic.wav");

    } // init method


    /*The start method
    Void: return no values
    Start a new thread called th
    */
    public void start ()
    {
    	// define a new thread
    	Thread th = new Thread (this);
    	// start this thread
    	th.start ();
    }


    /*The stop method
    Void:returnnovalue
    Doesnothing,includedtocomplete start/stop/destroy
    */
    public void stop ()
    {
	//nothing right now
    }


    /*The destroy method
    void: no return value
    does nothing, included to complete start/stop/destroy
    */
    public void destroy ()
    {
	//nothing right now
    }


    /*The mouseClicked method
    void: no return value
    checks for when the mouse is pressed and released
    */
    public void mouseClicked (MouseEvent e)
    {

    }


    /*The mouseExited method
    void: no return value
    checks for when mouse leaves screen border
    */
    public void mouseExited (MouseEvent e)
    {
	// called when the pointer leaves the applet's rectangular area
    }


    /*The mouseEntered method
    void: no return value
    checks for when mouse enters screen
    */
    public void mouseEntered (MouseEvent e)
    {
	// called when the pointer enters the applet's rectangular area
    }


    /*The mousePressed method
    void: no return value
    when the mouse button is pressed down, makes isMousePressed=true
    */
    public void mousePressed (MouseEvent e)
    { // called after a button is pressed down
    	isMousePressed = true;
      System.out.println("Pressed");
    	e.consume ();
    }


    /*The mousereleased method
    void: no return value
    when the mouse button is released, makes isMousePressed=false
    */
    public void mouseReleased (MouseEvent e)
    {
    	// called after a button is released
    	isMousePressed = false;
    	e.consume ();
    }


    /*The mousemoved method
    void: no return value
    when the mouses position moves, track that position and show its coordinates
    */
    public void mouseMoved (MouseEvent e)
    {
	// called during motion when no buttons are down
    	mx = e.getX ();
    	my = e.getY ();
    	//show the position at which the mouse is at
    	showStatus ("Mouse at (" + mx + "," + my + ")");
    	//repaint position of mouse
    	repaint ();
    	e.consume ();
    }


    /*The mousedragged method
    void: no return value
    when the mouses position moves, track that position and show its coordinates
    */
    public void mouseDragged (MouseEvent e)
    {
	      // called during motion with buttons down
    	mx = e.getX ();
    	my = e.getY ();
    	//show the position at whic the mouse is located
    	showStatus ("Mouse at (" + mx + "," + my + ")");
    	//repaint status of mouse
    	repaint ();
    	e.consume ();
    }


    /*The actionperformed method
    void: no return value
    does nothing, included in actionevent method headers
    */
    public void actionPerformed (ActionEvent e)
    {

    }


    /*The main run method, where majority of changing values and parameters are implemented
    Void: no return value
    The center of this applet: run method:
    -runs 60 times per second
    -checks for gamestate changes
    -checks for if any buttons are pressed
    -automatically spawns enemy units
    -detects when unit icons are pressed
    -spawns units when gold is available and icon is pressed
    -detect collisions between units
    -detect collisions between towers
    -change states when collided
    -attack units and towers
    -gain exp and gold*/
    public void run ()
    {
	// lower ThreadPriority
	Thread.currentThread ().setPriority (Thread.MIN_PRIORITY);

	// run a long while (true) this means in our case "always"
	while (true)
	{
	    //when the gamestate is 1 (title screen)
	    if (gameState == 1)
	    {
    		//create button at "play", if pressed then
    		if ((mx > 350 && mx < 400) && (my > 335 && my < 350) && (isMousePressed == true)) //detect click at exit button
    		{
    		    //change gamestate to 2 (ingame)
    		    gameState = 2;
    		    //resize the window to game size
    		    resize (1600, 500);
    		    //loop the music clip
    		    mainMusic.loop ();
    		}
    		//create button at instructions, if pressed then
    		if ((mx > 305 && mx < 450) && (my > 365 && my < 380) && (isMousePressed == true))
    		{
    		    //change gamestate to 3, (show instrucitons)
    		    gameState = 3;
    		}
	    }
	    //if gamestate is 3 (show instructions)
	    if (gameState == 3)
	    {
		//create button at 'back to main menu'
		if ((mx > 270 && mx < 500) && (my > 540 && my < 560) && (isMousePressed == true))
		{
		    //if button is pressed, change gamestate back to 1 (menu)
		    gameState = 1;
		}
	    }
	    //if gamestate is 4 or 5, game has ended; showing victory/defeat screen
	    if (gameState == 4 || gameState == 5)
	    {
		//if 'play again' button is pressed
		if ((mx > 270 && mx < 500) && (my > 540 && my < 560) && (isMousePressed == true))
		{
		    //change gamestate to 2, play game again
		    gameState = 2;
		}
	    }

	    //if the gamestate is 2, (in game)
	    if (gameState == 2)
	    {
		//set notEnoughGold to false
		notEnoughGold = false;
		//set notenoughexp to false
		notEnoughExp = false;
		//walk count increase
		walkCount++;
		//convert walkcount to string
		Integer.toString (walkCount);
		//cooldown count incrase
		cooldownCount++;
		//conver cooldowncount to string
		Long.toString (cooldownCount);
		//convert gold to string
		Integer.toString (gold);
		//convert exp to string
		Integer.toString (exp);
		//make showtext 0 again
		showText = 0;

		//generate a random number from 1-5
		enemySpawn = ranNum.nextInt (5) + 1;

		//if there is no current cooldown and enemypop is less than/equal to 5 , spawn enemies
		if (cooldownCount >= (enemyCD + lastEnemyCD) && enemyPop <= 5 && era == 1)
		{
		    //if enemyspawn is 1/2 spawn a melee unit
		    if (enemySpawn == 1 || enemySpawn == 2) //melee
		    {
			//make enemyCd equal to current cooldowncount
			enemyCD = cooldownCount;
			//cooldown of melee is 360/60 seconds
			lastEnemyCD = 360;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is melee
			enemyTypeOfUnit [numberOfEUnits] = 1;
			numberOfEUnits++;

		    }
		    //if enemyspawn is 3/4 spawn a ranged unit
		    if (enemySpawn == 3 || enemySpawn == 4) //ranged
		    {
			//make enemyCD equal to current coolldowncount
			enemyCD = cooldownCount;
			lastEnemyCD = 420;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is ranged
			enemyTypeOfUnit [numberOfEUnits] = 2;
			numberOfEUnits++;


		    }
		    //if enemyspawn is 5 spawn tankk
		    if (enemySpawn == 5) //tank
		    {
			// make enemyCd equal to current cooldowncount
			enemyCD = cooldownCount;
			lastEnemyCD = 540;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is tank
			enemyTypeOfUnit [numberOfEUnits] = 3;
			numberOfEUnits++;
		    }
		}

		//if era=2 and enough population and cd is off
		if (cooldownCount >= (enemyCD + lastEnemyCD) && enemyPop <= 6 && era == 2)
		{
		    //spawn a swordman
		    if (enemySpawn == 1 || enemySpawn == 2) //melee
		    {
			//make enemyCd equal to current cooldowncount
			enemyCD = cooldownCount;
			lastEnemyCD = 420;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is melee
			enemyTypeOfUnit [numberOfEUnits] = 4;
			numberOfEUnits++;

		    }
		    //spawn a rifle man
		    if (enemySpawn == 3 || enemySpawn == 4) //ranged
		    {
			//make enemyCd equal to current cooldowncount
			enemyCD = cooldownCount;
			lastEnemyCD = 480;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is ranged
			enemyTypeOfUnit [numberOfEUnits] = 5;
			numberOfEUnits++;


		    }
		    //if enemyspawn is 5 spawn a horse
		    if (enemySpawn == 5) //tank
		    {
			//make enemyCd equal to current cooldowncount
			enemyCD = cooldownCount;
			lastEnemyCD = 600;
			spawnUnit = true;
			//increase total units
			totalUnits++;
			//increase enemypop
			enemyPop++;
			//set unit rect to right side of screen
			enemyRectX [numberOfEUnits] = 1480;
			//set state to 0
			enemyState [numberOfEUnits] = 0;
			enemyXPos [numberOfEUnits] = 1480;
			//enemytype of unit is tank
			enemyTypeOfUnit [numberOfEUnits] = 6;
			numberOfEUnits++;
		    }
		}

		//Player spawning:
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 45 && mx <= 91) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 120)) && playerPop <= 8 && gold >= 15 && era == 1)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 15;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    //set player state to walking
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is melee
		    typeOfUnit [numberOfPUnits] = 1;
		    numberOfPUnits++;
		}
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 91 && mx <= 139) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 150)) && playerPop <= 8 && gold >= 25 && era == 1)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 25;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    //set player state to walking
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is ranged
		    typeOfUnit [numberOfPUnits] = 2;
		    numberOfPUnits++;
		}
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 139 && mx <= 185) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 180)) && playerPop <= 8 && gold >= 100 && era == 1)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 100;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    //set player state to walking
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is tank
		    typeOfUnit [numberOfPUnits] = 3;
		    numberOfPUnits++;
		}
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 45 && mx <= 91) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 150)) && playerPop <= 8 && gold >= 50 && era == 2)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 50;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    //set player state to walking
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is melee
		    typeOfUnit [numberOfPUnits] = 4;
		    numberOfPUnits++;
		}
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 91 && mx <= 139) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 180)) && playerPop <= 8 && gold >= 75 && era == 2)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 75;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    //set player state to walking
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is ranged
		    typeOfUnit [numberOfPUnits] = 5;
		    numberOfPUnits++;
		}
		//if mouse is pressed at melee icon and enough resources
		if ((mx > 139 && mx <= 185) && (my <= 50) && (isMousePressed == true) && (cooldownCount > (playerCD + 240)) && playerPop <= 8 && gold >= 500 && era == 2)
		{
		    //setcooldown
		    playerCD = cooldownCount;
		    spawnUnit = true;
		    //increase total units
		    totalUnits++;
		    //increase player pop
		    playerPop++;
		    //deduct gold
		    gold -= 500;
		    //set player initial position to -1
		    playerRectX [numberOfPUnits] = -1;
		    playerState [numberOfPUnits] = 0;
		    playerXPos [numberOfPUnits] = -1;
		    //type of unit is tank
		    typeOfUnit [numberOfPUnits] = 6;
		    numberOfPUnits++;
		}

		//if there isnt enough gold for any units, make notenoughgold = true; will display a message
		if ((mx > 45 && mx <= 91) && (my <= 50) && gold < 15 && era == 1)
		    notEnoughGold = true;
		if ((mx > 91 && mx <= 139) && (my <= 50) && gold < 25 && era == 1)
		    notEnoughGold = true;
		if ((mx > 139 && mx <= 185) && (my <= 50) && gold < 100 && era == 1)
		    notEnoughGold = true;
		if ((mx > 45 && mx <= 91) && (my <= 50) && gold < 50 && era == 2)
		    notEnoughGold = true;
		if ((mx > 91 && mx <= 139) && (my <= 50) && gold < 75 && era == 2)
		    notEnoughGold = true;
		if ((mx > 139 && mx <= 185) && (my <= 50) && gold < 500 && era == 2)
		    notEnoughGold = true;

		//if there are playerunits present on screen
		if (playerPop > 0)
		{
		    largest = Integer.MIN_VALUE;
		    for (int x = 0 ; x < numberOfPUnits ; x++)
		    {
			//cycle through player units to find leader (furthest right)
			if (playerXPos [x] > largest)
			{
			    largest = playerXPos [x];
			    //make playerlead farthese right
			    playerLead = x;
			}
		    }
		}

		//if there are enemyunits present on screen
		if (enemyPop > 0)
		{
		    smallest = Integer.MAX_VALUE;
		    for (int x = 0 ; x < numberOfEUnits ; x++)
		    {
			if (enemyXPos [x] < smallest)
			{
			    //cycle through enemy units to find leader (fursthest left)
			    smallest = enemyXPos [x];
			    //make enemylead farthest left
			    enemyLead = x;
			}
		    }
		}
		//if no units present
		if (playerPop == 0)
		{
		    //make the tower the leader
		    playerLead = 500;
		}
		//if not enemy units presents
		if (enemyPop == 0)
		{
		    //make the tower the leader
		    enemyLead = 500;
		}
		//convert to playerlead/enemylaed to strings to be printed
		Integer.toString (playerLead);
		Integer.toString (enemyLead);

		//loop from 0 to totalunits ; check all units
		for (int x = 1 ; x <= totalUnits ; x++)
		{
		    //secondary loop
		    for (int y = 1 ; y <= totalUnits ; y++)
			//player to enemy detection
			{
			    //get the widths of units
			    if (typeOfUnit [x - 1] == 1 || typeOfUnit [x - 1] == 4)
				width1 = 80;
			    if (typeOfUnit [x - 1] == 2 || typeOfUnit [x - 1] == 5)
				width1 = 62;
			    if (typeOfUnit [x - 1] == 3 || typeOfUnit [x - 1] == 6)
				width1 = 120;
			    //get widths of hitboxes
			    if (typeOfUnit [y - 1] == 1 || typeOfUnit [y - 1] == 4)
				width2 = 80;
			    if (typeOfUnit [y - 1] == 2 || typeOfUnit [y - 1] == 5)
				width2 = 62;
			    if (typeOfUnit [y - 1] == 3 || typeOfUnit [y - 1] == 6)
				width2 = 120;

			    //when any playerunit collides with an enemy unit, change the players state to attacking
			    if (playerRectX [x - 1] + width1 == playerRectX [y - 1] || playerRectX [x - 1] + width1 == enemyRectX [y - 1])
			    {
				playerState [x - 1] = 1;
			    }
			}

		    //secondary loop
		    for (int y = 1 ; y <= totalUnits ; y++)
			//enemy to player detection
			{
			    //get the widths of hitboxes
			    if (enemyTypeOfUnit [x - 1] == 1 || enemyTypeOfUnit [x - 1] == 4)
				width1 = 80;
			    if (enemyTypeOfUnit [x - 1] == 2 || enemyTypeOfUnit [x - 1] == 5)
				width1 = 62;
			    if (enemyTypeOfUnit [x - 1] == 3 || enemyTypeOfUnit [x - 1] == 6)
				width1 = 120;
			    //get width of hitboxes
			    if (enemyTypeOfUnit [y - 1] == 1 || enemyTypeOfUnit [y - 1] == 4)
				width2 = 80;
			    if (enemyTypeOfUnit [y - 1] == 2 || enemyTypeOfUnit [y - 1] == 5)
				width2 = 62;
			    if (enemyTypeOfUnit [y - 1] == 3 || enemyTypeOfUnit [y - 1] == 6)
				width2 = 120;

			    //when any enemy unit collides with a player unit, change the enemys state to attacking
			    if (enemyRectX [x - 1] == enemyRectX [y - 1] + width2 || enemyRectX [x - 1] == playerRectX [y - 1] + width2)
			    {
				enemyState [x - 1] = 1;
			    }
			}
		}

		//loop the amount of times as numberofPUnits
		for (int x = 1 ; x <= numberOfPUnits + 1 ; x++)
		{
		    //create temporary int
		    int temporary;
		    temporary = 1;

		    //if the players state is 1, they are stationary
		    if (playerState [x - temporary] == 1)
		    {
			//set x_speed to 0
			x_speed = 0;
			//playerRectX changed by x_speed
			playerRectX [x - temporary] += x_speed;
			//playerXPOs changes by x speed
			playerXPos [x - temporary] += x_speed;
		    }

		    // if the players state is 0, they are walking
		    else if (playerState [x - temporary] == 0)
		    {
			//set x_speed to 1
			x_speed = 1;
			//playerRectx changes by x speed
			playerRectX [x - temporary] += x_speed;
			//playerxpos changes by xspeed
			playerXPos [x - temporary] += x_speed;
		    }
		}

		//loop the amount of time as numberofEUNits
		for (int x = 1 ; x <= numberOfEUnits + 1 ; x++)
		{
		    //create temporary int
		    int temporary;
		    temporary = 1;

		    //if enemies state is 1
		    if (enemyState [x - temporary] == 1)
		    {
			//set xspeed to 0
			x_speed = 0;
			//enemyrectx changes by xspeed
			enemyRectX [x - temporary] -= x_speed;
			//enemyxpos changes by xspeed
			enemyXPos [x - temporary] -= x_speed;
		    }

		    else if (enemyState [x - temporary] == 0)
		    {
			//set xspeed to 0
			x_speed = 1;
			//enemyrectx changes by xspeed
			enemyRectX [x - temporary] -= x_speed;
			//enemyxpos changes by xspeed
			enemyXPos [x - temporary] -= x_speed;
		    }
		}

		//get the widths of playerLeader hit box
		if (typeOfUnit [playerLead] == 1 || typeOfUnit [playerLead] == 4)
		    //melee hitbox
		    width1 = 80;
		if (typeOfUnit [playerLead] == 2 || typeOfUnit [playerLead] == 5)
		    //ranged hitbox
		    width1 = 62;
		if (typeOfUnit [playerLead] == 3 || typeOfUnit [playerLead] == 6)
		    //tank hitbox
		    width1 = 120;

		//if the player leaders hitbox collides with enemyleaderes hit box
		if (playerRectX [playerLead] + (width1 - 3) < enemyRectX [enemyLead])
		{
		    //make all other units stop
		    for (int x = 0 ; x < totalUnits ; x++)
			//make all players moving state
			playerState [x] = 0;
		}
		//if no player units are present and enemy lead isnt at tower
		if (playerLead == 500 && enemyRectX [enemyLead] > 120)
		{
		    //make all enemy units to moving state
		    for (int x = 0 ; x < totalUnits ; x++)
			enemyState [x] = 0;
		}

		//if playerleader collides with e
		if ((playerRectX [playerLead] + (width1 - 3) < enemyRectX [enemyLead]) && (playerRectX [playerLead] + (width1 + 3) > enemyRectX [enemyLead]))
		{
		    //player state 1
		    playerState [playerLead] = 1;
		    //enemystate 1
		    enemyState [enemyLead] = 1;
		}
		//if enemy is approaching
		if (enemyRectX [enemyLead] > playerRectX [playerLead] + 125)
		{
		    //enemystate 0
		    enemyState [enemyLead] = 0;
		}
		//if enemy is at playerrextx
		if (enemyRectX [enemyLead] == playerRectX [playerLead] + 140)
		{
		    //enemystate 1
		    enemyState [enemyLead] = 1;
		}
		//if playerunit is at enemy tower
		if ((playerRectX [playerLead] + (width1 - 3) < enemyRectX [500]) && (playerRectX [playerLead] + (width1 + 3) > enemyRectX [500]))
		{
		    //player state 1
		    playerState [playerLead] = 1;
		}

		//if playerleader and enemyleader are fighting
		if ((playerState [playerLead] == 1 && enemyState [enemyLead] == 1))
		{
		    //if playerunit hasnt been assigned values
		    if (playerHP [playerLead] == 0)
		    {
			//if melee era1
			if (typeOfUnit [playerLead] == 1)
			{
			    //100 hp
			    playerHP [playerLead] = 100;
			    //21 dmg
			    playerDMG [playerLead] = 21;
			}
			//ranged era1
			if (typeOfUnit [playerLead] == 2)
			{
			    //80 hp
			    playerHP [playerLead] = 80;
			    //35 dmg
			    playerDMG [playerLead] = 35;
			}
			//tank era1
			if (typeOfUnit [playerLead] == 3)
			{
			    //150 hp
			    playerHP [playerLead] = 150;
			    // 41 dmg
			    playerDMG [playerLead] = 41;
			}
			//melee era2
			if (typeOfUnit [playerLead] == 4)
			{
			    //180 hp
			    playerHP [playerLead] = 180;
			    //35 dmg
			    playerDMG [playerLead] = 35;
			}
			//ranged era2
			if (typeOfUnit [playerLead] == 5)
			{
			    //120 hp
			    playerHP [playerLead] = 120;
			    //54 dmg
			    playerDMG [playerLead] = 54;
			}
			//tank era2
			if (typeOfUnit [playerLead] == 6)
			{
			    //300 hp
			    playerHP [playerLead] = 300;
			    //61 dmg
			    playerDMG [playerLead] = 61;
			}
		    }

		    //if enemylead hasnt been given values
		    if (enemyHP [enemyLead] == 0)
		    {
			//if melee era1
			if (enemyTypeOfUnit [enemyLead] == 1)
			{
			    //100 hp 21 dmg
			    enemyHP [enemyLead] = 100;
			    enemyDMG [enemyLead] = 21;
			}
			//if ranged era1
			if (enemyTypeOfUnit [enemyLead] == 2)
			{
			    //80 hp 35 dmg
			    enemyHP [enemyLead] = 80;
			    enemyDMG [enemyLead] = 35;
			}
			//if tank era1
			if (enemyTypeOfUnit [enemyLead] == 3)
			{
			    //200hp 41 dmg
			    enemyHP [enemyLead] = 200;
			    enemyDMG [enemyLead] = 41;
			}
			//if melee era2
			if (enemyTypeOfUnit [enemyLead] == 4)
			{
			    //180 hp 35 dmg
			    enemyHP [enemyLead] = 180;
			    enemyDMG [enemyLead] = 35;
			}
			//if ranged era2
			if (enemyTypeOfUnit [enemyLead] == 5)
			{
			    //120 hp 54 dmg
			    enemyHP [enemyLead] = 120;
			    enemyDMG [enemyLead] = 54;
			}
			//if rank era2
			if (enemyTypeOfUnit [enemyLead] == 6)
			{
			    //340 hp 61 dmg
			    enemyHP [enemyLead] = 340;
			    enemyDMG [enemyLead] = 61;
			}
		    }

		    //when walkcount is at 30, units receive damage
		    if (walkCount == 30)
		    {
			//playerhp minus enemydmg
			playerHP [playerLead] -= enemyDMG [enemyLead];
			//enemyhp minus player dmg
			enemyHP [enemyLead] -= playerDMG [playerLead];
		    }
		}

		//if enemyleader is attacking tower
		if ((enemyState [enemyLead] == 1 && playerLead == 500))
		{
		    if (enemyHP [enemyLead] == 0)
		    {
			//assign damage values to units
			if (enemyTypeOfUnit [enemyLead] == 1) //era1 melee
			{
			    enemyDMG [enemyLead] = 21;
			}
			if (enemyTypeOfUnit [enemyLead] == 2) //era1 ranged
			{
			    enemyDMG [enemyLead] = 35;
			}
			if (enemyTypeOfUnit [enemyLead] == 3) //era1 tank
			{
			    enemyDMG [enemyLead] = 41;
			}
			if (enemyTypeOfUnit [enemyLead] == 4) //era2 melee
			{
			    enemyDMG [enemyLead] = 35;
			}
			if (enemyTypeOfUnit [enemyLead] == 5) //era2 ranged
			{
			    enemyDMG [enemyLead] = 54;
			}
			if (enemyTypeOfUnit [enemyLead] == 6) //era2 tank
			{
			    enemyDMG [enemyLead] = 61;
			}
		    }

		    //when walk count is 30
		    if (walkCount == 30)
		    {
			//tower receives damage
			playerHP [500] -= enemyDMG [enemyLead];
		    }
		}

		//if playerlead is attacking enemy tower
		if ((playerState [playerLead] == 1 && enemyLead == 500))
		{
		    //asign players damage
		    if (playerHP [playerLead] == 0)
		    {
			if (typeOfUnit [playerLead] == 1) //era1 melee
			{
			    playerDMG [playerLead] = 21;
			}
			if (typeOfUnit [playerLead] == 2) //era1 ranged
			{
			    playerDMG [playerLead] = 35;
			}
			if (typeOfUnit [playerLead] == 3) //era1 tank
			{
			    playerDMG [playerLead] = 41;
			}
			if (typeOfUnit [playerLead] == 4) //era1 melee
			{
			    playerDMG [playerLead] = 35;
			}
			if (typeOfUnit [playerLead] == 5) //era1 ranged
			{
			    playerDMG [playerLead] = 54;
			}
			if (typeOfUnit [playerLead] == 6) //era1 tank
			{
			    playerDMG [playerLead] = 61;
			}
		    }

		    //when walkcount is 30
		    if (walkCount == 30)
		    {
			//enemytower receives damage
			enemyHP [500] -= playerDMG [playerLead];
		    }
		}

		//if player leader dies
		if (playerHP [playerLead] < 0)
		{
		    //deduct player pop
		    playerPop--;
		    //put their state to 2, dead
		    playerState [playerLead] = 2;
		    //move them off screen
		    playerXPos [playerLead] = Integer.MIN_VALUE;
		    playerRectX [playerLead] = Integer.MIN_VALUE;
		    //change player after them to moving state
		    playerState [playerLead + 1] = 0;
		}
		//if enemyleader dies
		if (enemyHP [enemyLead] < 0)
		{
		    //changed state to 2, dead
		    enemyState [enemyLead] = 2;
		    //move them off screen
		    enemyXPos [enemyLead] = Integer.MAX_VALUE;
		    enemyRectX [enemyLead] = Integer.MAX_VALUE;
		    //change player after them to moving state
		    enemyState [enemyLead + 1] = 0;

		    //if there is atleast 1 unit present
		    if (enemyPop > 0)
		    {
			//given gold and experience
			//melee era1
			if (enemyTypeOfUnit [enemyLead] == 1)
			{
			    //give 20 gold and 50 xp
			    gold += 20;
			    exp += 50;
			}
			//ranged era1
			if (enemyTypeOfUnit [enemyLead] == 2)
			{
			    //give 32 gold and 70 xp
			    gold += 32;
			    exp += 70;
			}
			//tank era1
			if (enemyTypeOfUnit [enemyLead] == 3)
			{
			    //give 130 gold , 270 xp
			    gold += 130;
			    exp += 270;
			}
			//melee era1
			if (enemyTypeOfUnit [enemyLead] == 4)
			{
			    //give 65 gold, 130 xp
			    gold += 65;
			    exp += 130;
			}
			//ranged era1
			if (enemyTypeOfUnit [enemyLead] == 5)
			{
			    //give 98 gold, 200 xp
			    gold += 98;
			    exp += 200;
			}
			//tank era1
			if (enemyTypeOfUnit [enemyLead] == 6)
			{
			    //give 650 gold , 1300 xp
			    gold += 650;
			    exp += 1300;
			}
			else
			{
			    gold += 0;
			    exp += 0;
			}
		    }
		    //increaes the population of enemy units
		    enemyPop--;
		}

		//if player tower dies
		if (playerHP [500] <= 0)
		{
		    //move all units off the screen
		    for (int x = 0 ; x <= totalUnits ; x++)
		    {
			//put all playerposx and their hitboxes off the screen
			playerXPos [x] = Integer.MIN_VALUE;
			playerState [x] = 2;
			//put all enemyposx and hitboxes off the screen
			enemyXPos [x] = Integer.MIN_VALUE;
			enemyState [x] = 2;
		    }
		    //change gamestate to 4 ( defeat )
		    gameState = 4;
		    //resize screen to end screen
		    resize (772, 616);
		}
		//if enemy tower is killed
		if (enemyHP [500] <= 0)
		{
		    //move all units off screen
		    for (int x = 0 ; x <= totalUnits ; x++)
		    {
			//move player units off screen
			playerXPos [x] = Integer.MIN_VALUE;
			playerState [x] = 2;
			//move enemy units off screen
			enemyXPos [x] = Integer.MIN_VALUE;
			enemyState [x] = 2;
		    }
		    //change game state to 5 ( victory )
		    gameState = 5;
		    //resize the screen to end screen
		    resize (772, 616);
		}

		//if a playerunit slips through an enemy unit, collisionbug; end the program
		if (playerRectX [playerLead] > 1600)
		{
		    //stop applet
		    System.exit (0);
		}

		//if the exit button is pressed down
		if ((mx <= 45) && (my <= 50) && (isMousePressed == true))
		{

		}

		//if the evolve button is pressed and there is enough xp to evolve
		if ((mx > 185 && mx <= 230) && (my <= 50) && (isMousePressed == true) && exp >= 1500)
		{
		    //evovlve era to era 2
		    era = 2;
		}

		//if the evolve button pressed but not enough xp
		if ((mx > 185 && mx <= 235) && (my <= 50) && exp < 1500)
		{
		    //change not enough xp to true
		    notEnoughExp = true;
		}

		//if the mouse is over the melee icon and era is 1
		if ((mx > 45 && mx <= 91) && (my <= 50) && era == 1)
		    //showtext is 1
		    showText = 1;
		//if the mouse is over the ranged icon and era is 1
		if ((mx > 91 && mx <= 139) && (my <= 50) && era == 1)
		    showText = 2;
		//if the mouse is over the tank icon and era is 1
		if ((mx > 139 && mx <= 185) && (my <= 50) && era == 1)
		    showText = 3;
		//if the mouse is over the melee icon and era is 2
		if ((mx > 45 && mx <= 91) && (my <= 50) && era == 2)
		    showText = 4;
		//if the mouse is over the ranged icon and era is 2
		if ((mx > 91 && mx <= 139) && (my <= 50) && era == 2)
		    showText = 5;
		//if the mouse is over the tank icon and era is 2
		if ((mx > 139 && mx <= 185) && (my <= 50) && era == 2)
		    showText = 6;

		//walkcount is used to change animations, changes once every run; 60times per second
		//when walk count reaches 60
		if (walkCount == 60)
		{
		    //rest walkcount to 0
		    walkCount = 0;
		}

		// repaint the applet
		repaint ();

		try
		{
		    // Stop thread for 20 milliseconds
		    Thread.currentThread ().sleep (20);
		}
		catch (InterruptedException ex)
		{
		    // do nothing
		}

		// set ThreadPriority to maximum value
		Thread.currentThread ().setPriority (Thread.MAX_PRIORITY);
	    }
	}
    }


    /* PAINT METHOD
    VOID : RETURNS NO VALUE
    responsible for drawing  all the images on the screen as well as text
    -draws text according to which button is being hovered over; display units information
    -draws a player unit and its assigned form depending on walkcounts current value
    -draws the backgrounds and icons
    -implements double buffering
    -draws screens depending on which gamestate the game is in
    */
    public void paint (Graphics g)
    {
	//game state is in 1 (title screen)
	if (gameState == 1)
	{
	    //change font to 1
	    g.setFont (font1);
	    g.setColor (Color.WHITE);
	    //draw the main menu background
	    g.drawImage (Back, 0, 0, 772, 616, null);
	    //draw play button
	    g.drawString ("PLAY", 350, 350);
	    //draw instructions button
	    g.drawString ("INSTRUCTIONS", 305, 380);
	}
	//if gamestate is 3
	if (gameState == 3)
	{
	    //draw the instructions screen
	    g.drawImage (Instruction, 0, 0, 772, 616, null);

	}
	//game state is 4 (defeat)
	if (gameState == 4)
	{
	    //draw the background
	    g.drawImage (Back, 0, 0, 772, 616, null);
	    //draw string: defeat
	    g.drawString ("Defeat!", 340, 350);
	    //draw button: play again
	    g.drawString ("Play again", 305, 380);
	}
	//game state is 5 ( victory )
	if (gameState == 5)
	{
	    //draw the background
	    g.drawImage (Back, 0, 0, 772, 616, null);
	    //draw string : victory
	    g.drawString ("Victory!", 340, 350);
	    //draw button : play again
	    g.drawString ("Play again", 305, 380);
	}

	//if gamestate is 2 (in game)
	if (gameState == 2)
	{
	    //change font and color to 1
	    g.setColor (Color.BLACK);
	    g.setFont (font1);
	    //if era is 1
	    if (era == 1)
		//draw the first background for era 1
		g.drawImage (Background, 0, 0, 1600, 500, null);
	    //era is 2
	    if (era == 2)
		//draw the era 2 background
		g.drawImage (Background2, 0, 0, 1600, 500, null);
	    //when there isnt enough gold for a unit
	    if (notEnoughGold == true)
		//display message: not enough gold
		g.drawString ("Not enough gold for this unit!", 240, 50);
	    //when there isnt enough xp to evolve
	    if (notEnoughExp == true)
		//display message ; not enough exp
		g.drawString ("Not enough experience to evolve! 1500 exp required", 240, 50);

	    //set color to yellow, font to 2
	    g.setColor (Color.YELLOW);
	    g.setFont (font2);
	    //if showtext is 1
	    if (showText == 1)
	    {
		//draw textbox
		g.drawImage (textbox, 0, 52, 140, 90, null);
		//drawstring melee info
		g.drawString ("Clubman                 15 gold", 0, 64);
		//drawstring description of melee 1
		g.drawString ("Attack: Low", 0, 76);
		g.drawString ("Defense: Medium", 0, 88);
		g.drawString ("A simple caveman, good", 0, 106);
		g.drawString ("for defending towers", 0, 118);
	    }
	    if (showText == 2)
	    {
		//draw textbox
		g.drawImage (textbox, 48, 52, 140, 90, null);
		//drawstring ranged info
		g.drawString ("Slinger                    25 gold", 49, 64);
		//drawstring description of ranged 1
		g.drawString ("Attack: Medium", 49, 76);
		g.drawString ("Defense: Low", 49, 88);
		g.drawString ("A caveman slinger, strong", 49, 106);
		g.drawString ("offensive abilites", 49, 118);
	    }
	    if (showText == 3)
	    {
		//draw textbox
		g.drawImage (textbox, 93, 52, 140, 90, null);
		//drawstring tank info
		g.drawString ("Dino Rider           100 gold", 93, 64);
		//drawstring description of tank 1
		g.drawString ("Attack: High", 93, 76);
		g.drawString ("Defense: High", 93, 88);
		g.drawString ("A dino rider with strong ", 93, 106);
		g.drawString ("attacks and high defenses", 93, 118);
	    }
	    if (showText == 4)
	    {
		//draw textbox
		g.drawImage (textbox, 0, 52, 140, 90, null);
		//drawstring melee info
		g.drawString ("Swordman              50 gold", 0, 64);
		//drawstring description of melee 2
		g.drawString ("Attack: Low", 0, 76);
		g.drawString ("Defense: Medium", 0, 88);
		g.drawString ("A trained swordman, low", 0, 106);
		g.drawString ("attack but good defenses", 0, 118);

	    }
	    if (showText == 5)
	    {
		//draw textbox
		g.drawImage (textbox, 48, 52, 140, 90, null);
		//drawstring ranged info
		g.drawString ("Rifle Man               75 gold", 49, 64);
		//drawstring descriptioin of ranged 2
		g.drawString ("Attack: Medium", 49, 76);
		g.drawString ("Defense: Low", 49, 88);
		g.drawString ("A rifle man, strong ", 49, 106);
		g.drawString ("attacks but low defense", 49, 118);
	    }
	    if (showText == 6)
	    {
		//draw textbox
		g.drawImage (textbox, 93, 52, 140, 90, null);
		//drawstring tank info
		g.drawString ("Horse Rider          500 gold", 93, 64);
		//drawstring description of tank 2
		g.drawString ("Attack: High", 93, 76);
		g.drawString ("Defense: High", 93, 88);
		g.drawString ("A horse jockey, the  ", 93, 106);
		g.drawString ("ultimate warrior", 93, 118);
	    }
	    //change font to font 1
	    g.setFont (font1);
	    //print gold of player
	    g.drawString ("Gold: " + gold, 240, 30);
	    //print players experience
	    g.drawString ("Experience: " + exp, 370, 30);
	    g.setColor (Color.RED);
	    //print the players tower hp
	    g.drawString ("" + playerHP [500], 40, 180);
	    //print the enemy tower hp
	    g.drawString ("" + enemyHP [500], 1540, 180);

	    //**************    PLAYER UNITS   *********************************************************
	    //loop numberofpUnits times
	    for (int x = 0 ; x <= numberOfPUnits ; x++)
	    {

		/*********     STONE AGE     ****************************************************************/
		//if player is dead, dont draw them
		if (typeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//melee frame 1
		if (typeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 15) && playerState [x] == 0)
		    g.drawImage (SM1, playerXPos [x], 390, 80, 80, null);
		//melee frame 2
		if (typeOfUnit [x] == 1 && (walkCount > 15 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (SM3, playerXPos [x], 390, 80, 80, null);
		//melee frame 3
		if (typeOfUnit [x] == 1 && (walkCount > 30 && walkCount <= 45) && playerState [x] == 0)
		    g.drawImage (SM2, playerXPos [x], 390, 80, 80, null);
		//melee frame 4
		if (typeOfUnit [x] == 1 && (walkCount > 45 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (SM3, playerXPos [x], 390, 80, 80, null);
		//melee frame 0
		if (typeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 60) && (playerState [x] == 1) && (x != playerLead))   //stationary
		    g.drawImage (SM3, playerXPos [x], 390, 80, 80, null);
		//melee attack frame 1
		if (typeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (SMA2, playerXPos [x] + 20, 390, 80, 80, null);
		//melee attack frame 2
		if (typeOfUnit [x] == 1 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (SMA1, playerXPos [x], 390, 80, 80, null);


		//player is dead, dont draw
		if (typeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//ranged frame 1
		if (typeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 15) && playerState [x] == 0)
		    g.drawImage (SR1, playerXPos [x], 390, 65, 80, null);
		//ranged frame 2
		if (typeOfUnit [x] == 2 && (walkCount > 15 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (SR3, playerXPos [x], 390, 65, 80, null);
		//ranged frame 3
		if (typeOfUnit [x] == 2 && (walkCount > 30 && walkCount <= 45) && playerState [x] == 0)
		    g.drawImage (SR2, playerXPos [x], 390, 65, 80, null);
		//ranged frame 4
		if (typeOfUnit [x] == 2 && (walkCount > 45 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (SR3, playerXPos [x], 390, 65, 80, null);
		//ranged frame 0
		if (typeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 1 && (x != playerLead))  //stationary
		    g.drawImage (SR3, playerXPos [x], 390, 65, 80, null);
		//ranged attack frame 1
		if (typeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (SR3, playerXPos [x], 390, 65, 80, null);
		//ranged attack frame 1
		if (typeOfUnit [x] == 2 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (SRA, playerXPos [x], 390, 65, 80, null);


		//player dead, dont draw
		if (typeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//tank frame 1
		if (typeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (ST1, playerXPos [x], 370, 115, 100, null);
		//tank frame 1
		if (typeOfUnit [x] == 3 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (ST2, playerXPos [x], 370, 115, 100, null);
		//tank frame 1
		if (typeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 1 && (x != playerLead)) //stationary
		    g.drawImage (ST2, playerXPos [x], 370, 115, 100, null);
		//tank attack frame 1
		if (typeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (STA, playerXPos [x], 360, 150, 115, null);
		//tank attack frame 2
		if (typeOfUnit [x] == 3 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (ST2, playerXPos [x], 370, 115, 100, null);


		// ****      MEDIEVAL AGE     *******************************************************************************************


		//unit dead, dont draw
		if (typeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//era 2 melee frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 15) && playerState [x] == 0)
		    g.drawImage (MDM1, playerXPos [x], 390, 80, 80, null);
		//era 2 melee frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount > 15 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (MDM2, playerXPos [x], 390, 80, 80, null);
		//era 2 melee frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount > 30 && walkCount <= 45) && playerState [x] == 0)
		    g.drawImage (MDM1, playerXPos [x], 390, 80, 80, null);
		//era 2 melee frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount > 45 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (MDM2, playerXPos [x], 390, 80, 80, null);
		//era 2 melee frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 60) && (playerState [x] == 1) && (x != playerLead))  //stationary
		    g.drawImage (MDM2, playerXPos [x], 390, 80, 80, null);
		//era 2 melee attack frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDMA2, playerXPos [x] + 15, 390, 80, 80, null);
		//era 2 melee atk frame, draw unit
		if (typeOfUnit [x] == 4 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDMA1, playerXPos [x] - 5, 360, 70, 110, null); //stationary pic


		//unit is dead, dont draw
		if (typeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//era 2 ranged frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 15) && playerState [x] == 0)
		    g.drawImage (MDR1, playerXPos [x], 390, 65, 80, null);
		//era 2 ranged frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount > 15 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (MDR2, playerXPos [x], 390, 65, 80, null);
		//era 2 ranged frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount > 30 && walkCount <= 45) && playerState [x] == 0)
		    g.drawImage (MDR1, playerXPos [x], 390, 65, 80, null);
		//era 2 ranged frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount > 45 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (MDR2, playerXPos [x], 390, 65, 80, null);
		//era 2 ranged frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 1 && (x != playerLead))  //stationary
		    g.drawImage (MDR2, playerXPos [x], 390, 65, 80, null);
		//era 2 ranged atk frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDR2, playerXPos [x], 390, 65, 80, null);  //attack 1
		//era 2 ranged atk frame, draw unit
		if (typeOfUnit [x] == 5 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDRA, playerXPos [x], 390, 65, 80, null);   //atck 2


		//unit dead, dont draw
		if (typeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 2)  ///DEAD
		{
		}
		//era 2 tank frame, draw unit
		if (typeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 0)
		    g.drawImage (MDT1, playerXPos [x], 370, 115, 100, null);
		//era 2 tank frame, draw unit
		if (typeOfUnit [x] == 6 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 0)
		    g.drawImage (MDT2, playerXPos [x], 370, 115, 100, null);
		//era 2 tank frame, draw unit
		if (typeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 60) && playerState [x] == 1 && (x != playerLead))  //stationary
		    g.drawImage (MDT2, playerXPos [x], 370, 115, 100, null);
		//era 2 tank atk frame, draw unit
		if (typeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 30) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDTA, playerXPos [x] + 25, 370, 135, 100, null);
		//era 2 tank atk frame, draw unit
		if (typeOfUnit [x] == 6 && (walkCount > 30 && walkCount <= 60) && playerState [x] == 1 && (x == playerLead))
		    g.drawImage (MDT2, playerXPos [x], 370, 115, 100, null);

	    }



	    // **********************************************************************************
	    //*****************ENEMY UNITS *********************************

	    for (int x = 0 ; x <= numberOfEUnits ; x++)
	    {

		//*****************   ENEMY STONE AGE    *************************************************


		//dead, dont draw
		if (enemyTypeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)
		{
		}
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 15) && enemyState [x] == 0)
		    g.drawImage (ESM1, enemyXPos [x], 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount > 15 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (ESM3, enemyXPos [x], 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount > 30 && walkCount <= 45) && enemyState [x] == 0)
		    g.drawImage (ESM2, enemyXPos [x], 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount > 45 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (ESM3, enemyXPos [x], 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 1 && (x != enemyLead))  //stationary
		    g.drawImage (ESM3, enemyXPos [x], 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (ESMA2, enemyXPos [x] - 20, 390, 80, 80, null);
		//era 1 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 1 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (ESMA1, enemyXPos [x], 390, 80, 80, null);


		//dead dont draw
		if (enemyTypeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)
		{
		}
		//era 1 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 15) && enemyState [x] == 0)
		    g.drawImage (ESR1, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount > 15 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (ESR3, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount > 30 && walkCount <= 45) && enemyState [x] == 0)
		    g.drawImage (ESR2, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount > 45 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (ESR3, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 1 && (x != enemyLead))  //stationary
		    g.drawImage (ESR3, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged attk frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (ESR3, enemyXPos [x], 390, 65, 80, null);
		//era 1 enemy ranged attk frame, draw unit
		if (enemyTypeOfUnit [x] == 2 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (ESRA, enemyXPos [x], 390, 65, 80, null);


		//dead dont draw
		if (enemyTypeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)  ///0-60 61-120
		{
		}
		//era 1 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (EST1, enemyXPos [x], 370, 115, 100, null);
		//era 1 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 3 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (EST2, enemyXPos [x], 370, 115, 100, null);
		//era 1 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 1 && (x != enemyLead)) //stationary
		    g.drawImage (EST2, enemyXPos [x], 370, 115, 100, null);
		//era 1 enemy tank atk frame, draw unit
		if (enemyTypeOfUnit [x] == 3 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (ESTA, enemyXPos [x] - 40, 360, 150, 115, null);
		//era 1 enemy tank  attk frame, draw unit
		if (enemyTypeOfUnit [x] == 3 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EST2, enemyXPos [x], 370, 115, 100, null);


		//***********    ENEMY MEDIEVAL AGE      *************************************************


		//dead, dont draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)  ///DEAD
		{
		}
		//era 2 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 15) && enemyState [x] == 0)
		    g.drawImage (EMDM1, enemyXPos [x], 390, 80, 80, null);
		//era 2 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount > 15 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (EMDM2, enemyXPos [x], 390, 80, 80, null);
		//era 2 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount > 30 && walkCount <= 45) && enemyState [x] == 0)
		    g.drawImage (EMDM1, enemyXPos [x], 390, 80, 80, null);
		//era 2 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount > 45 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (EMDM2, enemyXPos [x], 390, 80, 80, null);
		//era 2 enemy melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 60) && (enemyState [x] == 1) && (x != enemyLead))  //stationary
		    g.drawImage (EMDM2, enemyXPos [x], 390, 80, 80, null);
		//era 2 enemy atk melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDMA2, enemyXPos [x] - 15, 390, 80, 80, null);
		//era 2 enemy atk melee frame, draw unit
		if (enemyTypeOfUnit [x] == 4 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDMA1, enemyXPos [x] + 5, 360, 70, 110, null);


		//dead dont draw
		if (enemyTypeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)  ///DEAD
		{
		}
		//era 2 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 15) && enemyState [x] == 0)
		    g.drawImage (EMDR1, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount > 15 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (EMDR2, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount > 30 && walkCount <= 45) && enemyState [x] == 0)
		    g.drawImage (EMDR1, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount > 45 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (EMDR2, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 1 && (x != enemyLead))  //stationary
		    g.drawImage (EMDR2, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged atk frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDR2, enemyXPos [x], 390, 65, 80, null);
		//era 2 enemy ranged atk frame, draw unit
		if (enemyTypeOfUnit [x] == 5 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDRA, enemyXPos [x], 390, 65, 80, null);   //stationary


		//dead dont draw
		if (enemyTypeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 2)  ///DEAD
		{
		}
		//era 2 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 0)
		    g.drawImage (EMDT1, enemyXPos [x], 370, 115, 100, null);
		//era 2 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 6 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 0)
		    g.drawImage (EMDT2, enemyXPos [x], 370, 115, 100, null);
		//era 2 enemy tank frame, draw unit
		if (enemyTypeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 60) && enemyState [x] == 1 && (x != enemyLead))  //stationary
		    g.drawImage (EMDT2, enemyXPos [x], 370, 115, 100, null);
		//era 2 enemy atk tank frame, draw unit
		if (enemyTypeOfUnit [x] == 6 && (walkCount >= 0 && walkCount <= 30) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDTA, enemyXPos [x] - 25, 370, 135, 100, null);
		//era 2 enemy atk tank frame, draw unit
		if (enemyTypeOfUnit [x] == 6 && (walkCount > 30 && walkCount <= 60) && enemyState [x] == 1 && (x == enemyLead))
		    g.drawImage (EMDT2, enemyXPos [x], 370, 115, 100, null);
	    }
	}

    } //paint method



    /** Update - Method, implements double buffering */
    public void update (Graphics g)
    {

	// initialize buffer
	if (dbImage == null)
	{
	    dbImage = createImage (this.getSize ().width, this.getSize ().height);
	    dbg = dbImage.getGraphics ();
	}


	// clear screen in background
	dbg.setColor (getBackground ());
	dbg.fillRect (0, 0, this.getSize ().width, this.getSize ().height);

	// draw elements in background
	dbg.setColor (getForeground ());
	paint (dbg);

	// draw image on the screen
	g.drawImage (dbImage, 0, 0, this);

	//paint g; reduces flickering and resize problems
	paint (g);

    }


    // method to handle key - down events
    public boolean keyDown (Event e, int key)
    {
	// user presses left cursor key
	if (key == Event.LEFT)
	{
	    // changing x - speed so that ball moves to the left side (x_speed negative)
	    x_speed = -1;
	}


	// DON'T FORGET (although it has no meaning here)
	return true;
    }
} // MovingBallApplet5 class
