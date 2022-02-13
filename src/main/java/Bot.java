import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

import mindustry.game.*;
import mindustry.content.*;
import mindustry.game.Schematic.*;
import arc.struct.*;
import mindustry.*;
import mindustry.world.*;
import arc.files.*;
import mindustry.core.*;
import mindustry.ctype.*;

public class Bot {
    public static Seq<Stile> tiles = new Seq<>();
    public static Seq<Stile> tiles2 = new Seq<>();
    public static Block blocked = Blocks.sorter;

    public static void main(String[] args) throws IOException{
        Version.enabled = false;
        Vars.schematics = new Schematics();
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();
        for (ContentType type : ContentType.values()) {
            for (Content content : Vars.content.getBy(type)) {
                try {
                    content.init();
                } catch (Throwable ignored) {
                }
            }
        }
        String sorterSch = "bXNjaAB4nGNgZGBkZGDJS8xNZeC52Hhh38WGi00Xtl5sYGRgK84vKkktYmBgYGQAgf9AwAAAdzMQsg==";
        Schematic sorterSchema = Schematics.readBase64(sorterSch);
        for (Stile gg : sorterSchema.tiles) {
            blocked = gg.block;
        }

                    BufferedImage bi = ImageIO.read(new File("image.png"));
                    int w = bi.getWidth();
                    int h = bi.getHeight();
                    int[][] pictureR = new int[w][h];
                    int[][] pictureG = new int[w][h];
                    int[][] pictureB = new int[w][h];
                    int counter = 0;
                    int[][] sorterConfig = new int[w][h];
                    int x = 1;
                    int y = 1;
                    int y1 = h;
                    if (blocked == null) {
                        System.out.println("nullblock");
                        return;
                    }
                    while (x < w) {
                        y = 0;
                        y1 = h;
                        while (y < h) {
                            int rgb = bi.getRGB(x, y);
                            pictureR[x][y] = (rgb >> 16) & 0xFF;
                            pictureG[x][y] = (rgb >> 8) & 0xFF;
                            pictureB[x][y] = (rgb) & 0xFF;
                            int id = colorToSorter(pictureR[x][y], pictureG[x][y], pictureB[x][y]);
                            byte a = 0;
                            Stile tiler = new Stile(blocked, x, y1, Vars.content.item(id), a);
                            tiles.add(tiler);
                            y++;
                            y1--;
                        }
                        x++;
                    }
                    for (Stile f : tiles) {
                        if (f.block == null) {
                            System.out.println(f.x + " " + f.y + " " + f.config + " " + f.rotation);
                            f.block = blocked;
                            tiles2.add(f);
                        }
                    }
                    for (Stile f : tiles2) {
                        if (f.block == null) {
                            System.out.println(f.x + "#" + f.y + " " + f.config + " " + f.rotation);
                        }
                    }
                    System.out.println("gen");
                    System.out.println(blocked.name);

                    int xSize = w%256==0 ? w : w+1;
                    int ySize = h%256==0 ? h : h+1;

                    for(x = 0; x < xSize; x+=256){
                        for(y = 0; y < ySize; y+=256) {
                            generateFileSchematic(tiles,x,y,(x+256)<=w?256:w-(w-(x)),(y+256)<=h?256:h-(h-(y)));
                        }
                    }
    }
    public static int colorToSorter(int r, int g, int b) {
        int itemR[] = new int[]{217, 147, 235, 178, 247, 39, 141, 249, 119, 83, 203, 224, 243, 116, 225, 225};
        int itemG[] = new int[]{157, 127, 238, 198, 203, 39, 161, 163, 119, 86, 217, 186, 233, 87, 121, 170};
        int itemB[] = new int[]{115, 169, 245, 210, 164, 39, 227, 199, 119, 92, 127, 110, 121, 206, 94, 95};

        int[] itemOut = new int[16];
        int minDif = 0;
        int i = 0;
        while (i < 16) {
            int t1 = 0;
            int t2 = 0;
            int t3 = 0;
            if (itemR[i] < r) {
                t1 = r - itemR[i];
            }
            if (itemR[i] > r) {
                t1 = itemR[i] - r;
            }
            if (itemR[i] == r) {
                t1 = 0;
            }
            if (itemG[i] < g) {
                t2 = g - itemG[i];
            }
            if (itemG[i] > g) {
                t2 = itemG[i] - g;
            }
            if (itemG[i] == g) {
                t2 = 0;
            }
            if (itemB[i] < b) {
                t3 = b - itemB[i];
            }
            if (itemB[i] > b) {
                t3 = itemB[i] - b;
            }
            if (itemB[i] == b) {
                t3 = 0;
            }
            itemOut[i] = t1 + t2 + t3;
            if (itemOut[i] < itemOut[minDif]) {
                minDif = i;
            }
            i++;
        }
        return minDif;
    }

    public static void generateFileSchematic(Seq<Stile> tilesAll, int startX, int startY, int offsetX, int offsetY) throws IOException{
        Seq<Stile> tiles = new Seq<>();
        tilesAll.forEach(t->{
                    if(t.x<startX+offsetX&&t.x>=startX&&t.y<startY+offsetY&&t.y>=startY){
                        Stile t1 = new Stile(t.block,t.x-offsetX,t.y-offsetY,t.config,t.rotation);
                        tiles.add(t1);
                    }
                }
        );
        StringMap tags = new StringMap();
        tags.put("name", "photo"+(startX/256)+"_"+(startY/256));
        Schematic schem = new Schematic(tiles, tags, offsetX, offsetY);
        schem.save();
        Schematics aaa = new Schematics();
        String outlast = aaa.writeBase64(schem);
        Schematics.write(schem, new Fi(new File("result/photo"+(startX/256)+"_"+(startY/256)+".msch")));
    }
}
