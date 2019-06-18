package calen;

import java.io.*;
import java.text.*;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import com.sun.javafx.geom.Rectangle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class Calendar extends Application
{
   private BorderPane containerPane = new BorderPane();
   private LocalDateTime date = LocalDateTime.now();
   private String appointmentFile = "src/project4/appointmentFile.csv";
   private Scene scene;
   private HBox txtGroup;
   private int curr_year;
   private int curr_month;
   private List<Label> active_click = new ArrayList();
   Button left, year, today, right;
   
   
   //Use this stage if you decide to complete the extra credit

   private Stage appointmentStage = new Stage();
   private Scene appointmentScene;
   private Parent appointmentPane = new StackPane();

   @Override
   public void start(Stage primaryStage)
   {
      scene = new Scene(containerPane, 1000, 800);
      containerPane.setStyle("-fx-background-color: whitesmoke;");
      setupTopPane();
      GridPane gp = setupMonthPane(date.getYear(), date.getMonthValue());
      containerPane.setCenter(gp);
        
      primaryStage.setTitle("Calendar");
      primaryStage.setMinWidth(1000);
      primaryStage.setMinHeight(800);
      primaryStage.setScene(scene);
      primaryStage.show();
        
        
      //Use the following if you decide to complete the extra credit

      
      
      appointmentScene = new Scene(appointmentPane, 350, 250);
      appointmentStage.setTitle("Add Event");
      appointmentStage.setScene(appointmentScene);

   }
    
   public void setupTopPane()
   {
	  BorderPane nav = new BorderPane();
      containerPane.setTop(nav);
      
      txtGroup = new HBox(20);
      Text tm = new Text(date.getMonth() + "");
      Text ty = new Text(date.getYear() + "");
      tm.setFont(Font.font("Arial",FontWeight.LIGHT, FontPosture.REGULAR, 20));
      ty.setFont(Font.font("Arial",FontWeight.LIGHT, FontPosture.REGULAR, 20));
      txtGroup.getChildren().addAll(tm,ty);
      nav.setLeft(txtGroup);
      
      HBox btnGroup = new HBox(10);
      left = new Button("<");
      year = new Button("    Year    ");
      today = new Button("    Today    ");
      right = new Button(">");
      btnGroup.getChildren().addAll(left,year,today,right);
      nav.setRight(btnGroup);
   
      nav.setPadding(new Insets(10,10,0,10));
   }
   
   public GridPane setupMonthPane(int yearValue, int monthValue)
   {
	  
	  curr_year = yearValue;
	  actionEvent(yearValue,monthValue);
      GridPane monthPane = new GridPane();
      
      
      for(int i=0;i<7;i++) 
      {
    	  Label m = new Label("S");
    	  switch(i)
    	  {
    	  	case 0:
    	  	case 6:
    	  		m.setText("S");
    	  		break;
    	  	case 1:
    	  		m.setText("M");
    	  		break;
    	  	case 2:
    	  	case 4:
    	  		m.setText("T");
    	  		break;
    	  	case 3:
    	  		m.setText("W");
    	  		break;
    	  	default:
    	  		m.setText("F");
    	  		break;
    	  }
    	  
          m.prefWidthProperty().bind(containerPane.widthProperty());
          m.setAlignment(Pos.CENTER);
          m.setFont(new Font(15));
          m.setStyle("-fx-border-color: black");
          
          monthPane.add(m, i, 0);
      }
      
      for(int i=1;i<7;i++) 
      {
    	  for(int j=0;j<7;j++)
    	  {
    		  Label d = new Label("");
    		  d.prefWidthProperty().bind(containerPane.widthProperty());
    		  d.prefHeightProperty().bind(containerPane.heightProperty());
    		  d.setStyle("-fx-border-color: black");
    		  
    		  monthPane.add(d, j, i);
    	  }
      }

      monthPane.setPadding(new Insets(10,10,20,10));
        
      fillUpMonth(monthPane, yearValue, monthValue);  
        
      return monthPane;
   }
   
   public void fillUpMonth(GridPane monthGP, int yearValue, int monthValue)
   {
	   int beginDay = 0;
	   int daysInMonth = 0;
	   int col = 0;
	   int row = 0;
	   int curr_day = date.getDayOfMonth();
	   LocalDateTime curr_date = date.withYear(yearValue);
	   curr_date = curr_date.withMonth(monthValue);
	   if(monthValue == 1)
	   {
		   YearMonth lastYear = YearMonth.of(yearValue-1, 12);
		   beginDay = lastYear.lengthOfMonth();
		   
		   YearMonth thisYear = YearMonth.of(yearValue, monthValue);
		   daysInMonth = thisYear.lengthOfMonth();
	   }
	   else 
	   {
		   YearMonth lastYear = YearMonth.of(yearValue, monthValue-1);
		   beginDay = lastYear.lengthOfMonth();
		   
		   YearMonth thisYear = YearMonth.of(yearValue, monthValue);
		   daysInMonth = thisYear.lengthOfMonth();
	   }
	   
	   LocalDateTime d = curr_date.withDayOfMonth(1);
	   LocalDateTime ld = curr_date.withDayOfMonth(daysInMonth);
        
	   ObservableList<Node> date_label = monthGP.getChildren();

	   int i = 1, j=1, k = beginDay - (d.getDayOfWeek().getValue()-1);
	   for(Node node : date_label) 
	   {
		  Label s = (Label)node;
		  s.setAlignment(Pos.CENTER);
		  s.setTextFill(Color.BLACK);
		  s.setFont(new Font(15));
		  if(monthGP.getRowIndex(node) > 0)
		  {
			  if(d.getDayOfWeek().getValue() > 6)
			  {
				  if(i==curr_day && (date.getMonth().getValue() == monthValue && date.getYear() == yearValue))
				  {
					  active_click.add(s);
					  col = monthGP.getColumnIndex(node);
					  row = monthGP.getRowIndex(node);
					  i++;
				  }
				  else 
				  {
					  if(monthGP.getRowIndex(node) > 0 && i<=daysInMonth) 
					  {		
						  active_click.add(s);
						  s.setText(i + "");
						  i++;
					  }
					  else if(i>daysInMonth) 
					  {
						  s.setText(j+"");
						  s.setTextFill(Color.GREY);
						  j++;
					  }
					  else {
						  s.setText(k+"");
						  s.setTextFill(Color.GREY);
						  k++;
					  } 
				  }
			  }
			  else
			  {
				  if(i==curr_day && (date.getMonth().getValue() == monthValue && date.getYear() == yearValue))
				  {
					  active_click.add(s);
					  col = monthGP.getColumnIndex(node);
					  row = monthGP.getRowIndex(node);
					  i++;
				  }
				  else
				  {
					  if((monthGP.getColumnIndex(node) >= d.getDayOfWeek().getValue() || monthGP.getRowIndex(node) > 1) && i<=daysInMonth) 
					  {			  		 
						active_click.add(s);
						s.setText(i + "");
						i++;
					  }
					  else if(i>daysInMonth) 
					  {
						s.setText(j+"");
						s.setTextFill(Color.GREY);
						j++;
					  }
					  else {
						s.setText(k+"");
						s.setTextFill(Color.GREY);
						k++;
					  }
				  }
				  
			  }
			  
		  }
	   }   
	   if(date.getMonth().getValue() == monthValue && date.getYear() == yearValue) 
	   {
		   Label curr_label = new Label(date.getDayOfMonth()+ "", new Circle(15,Color.web("FF0000")));
		   curr_label.setContentDisplay(ContentDisplay.CENTER);
		   curr_label.setTextFill(Color.WHITE);
		   curr_label.setAlignment(Pos.CENTER);
		   curr_label.prefWidthProperty().bind(containerPane.widthProperty());
		   curr_label.prefHeightProperty().bind(containerPane.heightProperty());
		   curr_label.setFont(new Font(20));
		   monthGP.add(curr_label, col, row);
	   }
	   
      try {
    		
    		for(Label s : active_click)
		      {
		    	  s.setOnMouseClicked(e -> {
		    		  if(e.getClickCount() == 2) {
		    			  setupAppointmentPane(monthGP,yearValue,monthValue,s);
		    		  }
		    	  });
		      }
    		displayAppointments(monthGP, yearValue,monthValue);
    	  } catch (Exception e) {
    			e.printStackTrace();
    	  }
        
   }
    
   public GridPane twelveMonthsPane()
   {
      GridPane twelve = new GridPane();

      
      Text m = (Text)txtGroup.getChildren().get(0);
      
      m.setText("");
      int month = 1;
      for(int i=0;i<3;i++)
      {
    	  for(int j=0;j<4;j++)
    	  {
    		  VBox temp = new VBox();
    		  Label t = new Label(new DateFormatSymbols().getMonths()[month-1]);
    		  t.prefWidthProperty().bind(temp.widthProperty());
    		  t.setAlignment(Pos.CENTER);
    		  GridPane remove = setupMonthPane(curr_year,month);
    		  
    		  for(Node node : remove.getChildren())
    		  {
    			  Label remove_label = (Label)node;
    			  remove_label.setStyle("-fx-border-color: whitesmoke;");
    		  }
    		  
    		  temp.getChildren().addAll(t,remove);
    		  twelve.add(temp, j, i);
    		  month++;
    	  }
      }
      
      
      actionEvent(curr_year,curr_month); 
        
        
      return twelve;
   }
   
   public void actionEvent(int yearValue,int monthValue) 
   {
	   left.setOnAction(e -> {
		   if(monthValue == 1)
		   {			   
			   containerPane.setCenter(setupMonthPane(yearValue-1,12));
			   Text m = (Text)txtGroup.getChildren().get(0);
			   Text y = (Text)txtGroup.getChildren().get(1);
			   y.setText((yearValue - 1) + "");
			   m.setText(new DateFormatSymbols().getMonths()[11].toUpperCase());
		   }
		   else 
		   {
			   containerPane.setCenter(setupMonthPane(yearValue,monthValue-1));
			   Text m = (Text)txtGroup.getChildren().get(0);
			   m.setText(new DateFormatSymbols().getMonths()[monthValue-2].toUpperCase());
		   }
	   });
	   right.setOnAction(e -> {
		   if(monthValue == 12)
		   {
			   containerPane.setCenter(setupMonthPane(yearValue+1,1));
			   Text m = (Text)txtGroup.getChildren().get(0);
			   Text y = (Text)txtGroup.getChildren().get(1);
			   y.setText((yearValue + 1) + "");
			   m.setText(new DateFormatSymbols().getMonths()[0].toUpperCase());
		   }
		   else 
		   {
			   containerPane.setCenter(setupMonthPane(yearValue,monthValue+1));
			   Text m = (Text)txtGroup.getChildren().get(0);
			   m.setText(new DateFormatSymbols().getMonths()[(monthValue)].toUpperCase());
		   }
	   });
	   year.setOnAction(e -> {
		   curr_year = yearValue;
		   curr_month = monthValue;
		   containerPane.setCenter(twelveMonthsPane());
	   });
	   today.setOnAction(e -> {
		   containerPane.setCenter(setupMonthPane(date.getYear(),date.getMonthValue()));
		   Text m = (Text)txtGroup.getChildren().get(0);
		   Text y = (Text)txtGroup.getChildren().get(1);
		   y.setText(date.getYear() + "");
		   m.setText(new DateFormatSymbols().getMonths()[date.getMonthValue()-1].toUpperCase());
	   });
   }
   
   
   //The following is for the extra credit
    
   public void setupAppointmentPane(GridPane monthPane, int yearValue,int monthValue, Label s)
   {
	  
	   Date d = new Date();
	   SimpleDateFormat simpDate = new SimpleDateFormat("kk");
	   SimpleDateFormat formatMin = new SimpleDateFormat("mm");
	   
	   appointmentStage.show();
	   StackPane root = (StackPane) appointmentPane;
	   FlowPane fp = new FlowPane();
	   fp.setAlignment(Pos.CENTER);
	   fp.setVgap(20);
	   fp.setOrientation(Orientation.VERTICAL);

	   HBox wrap_time = new HBox(15);
	   HBox wrap_submit = new HBox(20);
	   
	   TextField tf = new TextField();
	   Label title = new Label("Title:     ",tf);
	   title.setAlignment(Pos.TOP_CENTER);
	   title.setContentDisplay(ContentDisplay.RIGHT);
	   
	   Label time = new Label("Time:     ");
	   ComboBox<String> hour = new ComboBox();
	   for(int i=0;i<24;i++)
	   {
		   if(i<10)
			   hour.getItems().add("0" + i);
		   else
			   hour.getItems().add(i+"");
	   }
	   hour.setValue(simpDate.format(d)+"");
	   ComboBox<String> min = new ComboBox();
	   for(int i=0;i<60;i++)
	   {
		   if(i<10)
			   min.getItems().add("0" + i);
		   else
			   min.getItems().add(i+"");
	   }
	   min.setValue(formatMin.format(d));
	   wrap_time.getChildren().addAll(time, hour,min);
	   

	   Button clear = new Button("Clear");
	   Button submit = new Button("Submit");
	   wrap_submit.setPadding(new Insets(25,0,0,0));
	   wrap_submit.getChildren().addAll(clear,submit);
	   wrap_submit.setAlignment(Pos.BASELINE_RIGHT);
	   
	   fp.getChildren().addAll(title,wrap_time,wrap_submit);
	   root.getChildren().add(fp);
	  

	   clear.setOnMouseClicked(v -> {
		   tf.setText("");
		   hour.setValue(simpDate.format(d));
		   min.setValue(date.getMinute()+"");
	   });
	   
	   submit.setOnMouseClicked(v -> {
		   StringBuffer data = new StringBuffer();
		   data.append(tf.getText() + "," + yearValue + ",");
		   data.append(monthValue + "," + s.getText() + ",");
		   data.append(hour.getValue() + "," + min.getValue() + "\n");
		   storeAppointment(data.toString(),yearValue,monthValue);
		   appointmentStage.hide();
		   containerPane.setCenter(setupMonthPane(yearValue,monthValue));
	   });
   }
    
   public void displayAppointments(GridPane monthPane,int yearValue,int monthValue) throws Exception
   {
      List<List<String>> records = new ArrayList<>();
	  List<Integer> col = new ArrayList();
	  List<Integer> record_i = new ArrayList();
	  List<Integer> row = new ArrayList();
      try(BufferedReader br = new BufferedReader(new FileReader("./src/calen/appointmentFile.csv")))
      {
    	  String line;
    	  while((line = br.readLine()) != null) 
    	  {
    		  String[] values = line.split(",");
    		  records.add(Arrays.asList(values));
    	  }
      }
      
      for(int i=0;i<records.size();i++)
      {
    	  for(int j=0;j<records.get(i).size(); j++)
    	  {
    		  if(j == 1 || j == 2)
    		  {
    			  if(Integer.parseInt(records.get(i).get(j)) == yearValue && Integer.parseInt(records.get(i).get(j+1)) == monthValue)
        		  {  

    				  for(Node n : monthPane.getChildren())
    				  {
    					  Label t = (Label)n;

    					 
    					  if(!t.getText().isEmpty()  && (t.getTextFill() != Color.GREY) && (monthPane.getRowIndex(n) > 0) && (Integer.parseInt(records.get(i).get(3)) == Integer.parseInt(t.getText())))
    					  {
    						  col.add(monthPane.getColumnIndex(n));
    						  row.add(monthPane.getRowIndex(n));
    						  record_i.add(i);
    					  }
    				  }
        		  }
    		  }
    	  }
      }
      for(int i=0;i<record_i.size();i++)
      {
    	  String hour = (Integer.parseInt(records.get(record_i.get(i)).get(4)) < 10) ? ""+records.get(record_i.get(i)).get(4) : records.get(record_i.get(i)).get(4);
    	  String min = (Integer.parseInt(records.get(record_i.get(i)).get(5)) < 10) ? ""+records.get(record_i.get(i)).get(5) : records.get(record_i.get(i)).get(5);
    	  Label t = new Label(hour + ":" + min + " " + records.get(record_i.get(i)).get(0));
    	  t.setTextFill(Color.GREEN);
    	  t.setPadding(new Insets(0,0,20,0));
    	  t.setFont(new Font(15));
    	  t.prefWidthProperty().bind(containerPane.widthProperty());
		  t.prefHeightProperty().bind(containerPane.heightProperty());
    	  t.setAlignment(Pos.BOTTOM_CENTER);
    	  monthPane.add(t, col.get(i), row.get(i));
      }
   }

   

    
   public void storeAppointment(String data, int yearValue, int monthValue)
   {   
      try {
    	    FileWriter fw = new FileWriter("./src/calen/appointmentFile.csv",true);
    	    fw.write(data);
    	    fw.close();
    	    
      } catch (Exception e)	 {
    	 e.printStackTrace();
      }
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {
      launch(args);
   }
}
