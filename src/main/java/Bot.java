import java.util.ArrayList;
import java.util.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import mindustry.game.*;
import mindustry.content.*;
import mindustry.game.Schematic.*;
import mindustry.content.Items.*;
import arc.struct.*;
import mindustry.*;
import mindustry.world.*;
import arc.files.*;
import arc.math.*;
import arc.util.*;
import mindustry.world.blocks.distribution.Sorter;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;

public class Bot{
public static Array<Stile> tiles=new Array<>();
    public static Array<Stile> tiles2=new Array<>();
public static Block blocked=Blocks.sorter;
    public static void main(String[] args) {
      Version.enabled = false;
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();
        for(ContentType type : ContentType.values()){
            for(Content content : Vars.content.getBy(type)){
                try{
                    content.init();
                }catch(Throwable ignored){
                }
            }
        }
    String sirterSch="bXNjaAB4nGNgZGBkZGDJS8xNZeC52Hhh38WGi00Xtl5sYGRgK84vKkktYmBgYGQAgf9AwAAAdzMQsg==";
    Schematic sorterschema=Schematics.readBase64(sirterSch);
    for(Stile gg: sorterschema.tiles){blocked=gg.block;}
    try{        
    StringMap tags=new StringMap();
    tags.put("name","photo");
    BufferedImage bi = ImageIO.read( new File( "image.png" ) );
    int w=bi.getWidth();
    int h=bi.getHeight();
    if(w>550||h>550){System.out.println("Picture may be smaller than fucking large pixels");return;}
    int[][] pictureR=new int[w][h];
    int[][] pictureG=new int[w][h];
    int[][] pictureB=new int[w][h];
    int counter=0;
    int[][] sorterConfig=new int[w][h];
    int x=1;int y=1;
    int y1=h;
    if(blocked==null){System.out.println("nullblock");return;}
while(x<w){
System.out.print("x"+x);
y=0;y1=h;
while(y<h){

int rgb=bi.getRGB(x,y);
pictureR[x][y] = (rgb >> 16) & 0xFF; 
pictureG[x][y] = (rgb >> 8) & 0xFF; 
pictureB[x][y] = (rgb ) & 0xFF;
int id=colorToSorter(pictureR[x][y] ,pictureG[x][y] ,pictureB[x][y] );
byte a=0;
Stile tiler=new Stile(blocked,x,y1,id,a);
if(tiler==null){return;}
tiles.add(tiler);
y++;
y1--;
}
x++;
}//����� for

for(Stile f:tiles){if(f.block==null){System.out.println(f.x+" "+f.y+" "+f.config+" "+f.rotation);f.block=blocked;tiles2.add(f);}}
for(Stile f:tiles2){if(f.block==null){System.out.println(f.x+"#"+f.y+" "+f.config+" "+f.rotation);}}

File schemFile = new File("photo.msch");
                
System.out.println("gen");
System.out.println(blocked.name);
				Schematic schem=new Schematic(tiles,tags,w,h);
				schem.save();
				Schematics aaa=new Schematics();
         String outlast=aaa.writeBase64(schem);
         System.out.println(outlast);
                
                Schematics.write(schem, new Fi(schemFile));
                
                }
    
catch(IOException e){};}

public static int colorToSorter(int r, int g, int b){
	int itemR[]=new int[] {217,147,235,178,247,39,141,249,119,83,203,224,243,116,225,225};
	int itemG[]=new int[] {157,127,238,198,203,39,161,163,119,86,217,186,233,87,121,170};
	int itemB[]=new int[] {115,169,245,210,164,39,227,199,119,92,127,110,121,206,94,95};
	
	int[] itemOut=new int[16];
	int minDif=0;
	int i=0;
	while(i<16){
int t1=0;
int t2=0;
int t3=0;
if(itemR[i]<r){t1=r-itemR[i];}
if(itemR[i]>r){t1=itemR[i]-r;}
if(itemR[i]==r){t1=0;}
if(itemG[i]<g){t2=g-itemG[i];}
if(itemG[i]>g){t2=itemG[i]-g;}
if(itemG[i]==g){t2=0;}
if(itemB[i]<b){t3=b-itemB[i];}
if(itemB[i]>b){t3=itemB[i]-b;}
if(itemB[i]==b){t3=0;}
itemOut[i]=t1+t2+t3;
if(itemOut[i]<itemOut[minDif]){minDif=i;}
i++;}//����� for
System.out.print(minDif);
return minDif;
}//����� �������
}//�������� ������
