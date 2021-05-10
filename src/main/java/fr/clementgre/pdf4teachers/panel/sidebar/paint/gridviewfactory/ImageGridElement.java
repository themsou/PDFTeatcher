package fr.clementgre.pdf4teachers.panel.sidebar.paint.gridviewfactory;

import fr.clementgre.pdf4teachers.document.editions.elements.GraphicElement;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.panel.sidebar.paint.lists.ImageData;
import fr.clementgre.pdf4teachers.panel.sidebar.paint.lists.ImageLambdaData;
import fr.clementgre.pdf4teachers.utils.image.ExifUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Collections;

public class ImageGridElement extends ImageLambdaData{
    
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(null);
    private boolean rendering = false;
    private ExifUtils.BasicExifData exifData;
    
    private ImageData linkedImageData;
    
    public ImageGridElement(String imageId){
        super(imageId);
        loadExifData();
        setup();
    }
    public ImageGridElement(ImageData linkedImageData){
        super(linkedImageData.getImageId());
        this.linkedImageData = linkedImageData;
        loadExifData();
        setup();
    }
    public ImageGridElement(String imageId, ImageData linkedImageData, Image image){
        super(imageId);
        this.linkedImageData = linkedImageData;
        setImage(image);
        loadExifData();
        setup();
    }
    public ImageGridElement(String imageId, Image image){
        super(imageId);
        setImage(image);
        loadExifData();
        setup();
    }
    
    public void loadExifData(){
        // TODO: create thread for async
        exifData = new ExifUtils.BasicExifData(new File(imageId));
    }
    
    private void setup(){
    
    }
    
    public void toggleFavorite(){
        if(isFavorite()){
            MainWindow.paintTab.favouriteImages.getList().removeItems(Collections.singletonList(this));
            linkedImageData = null;
        }else{
            linkedImageData = new ImageData(imageId, 0, 0, GraphicElement.RepeatMode.KEEP_RATIO, GraphicElement.ResizeMode.CORNERS, 0, 0);
            MainWindow.paintTab.favouriteImages.getList().addItems(Collections.singletonList(this));
        }
        if(MainWindow.paintTab.galleryWindow != null && MainWindow.paintTab.galleryWindow.isShowing()){
            if(TR.tr("galleryWindow.filterAndEditCombo.favourites").equals(MainWindow.paintTab.galleryWindow.getList().getFilterType())){
                MainWindow.paintTab.galleryWindow.getList().updateItemsFiltered();
            }
        }
    }
    
    public void addToDocument(){
        if(MainWindow.mainScreen.hasDocument(false)) getImageData().addToDocument(hasLinkedImageData());
    }
    public void setAsToPlaceElement(){
        if(MainWindow.mainScreen.hasDocument(false)) getImageData().setAsToPlaceElement(hasLinkedImageData());
    }
    
    public ImageData getImageData(){
        if(hasLinkedImageData()){
            return linkedImageData;
        }else if(isFavorite()){
            return MainWindow.paintTab.favouriteImages.getList().getAllItems().get(MainWindow.paintTab.favouriteImages.getList().getAllItems().indexOf(this)).linkedImageData;
        }else{
            return toImageData();
        }
    }
    
    // SORTER
    
    public int compareTimeWith(ImageGridElement element){
        int value = element.getExifData().getEditDate().compareTo(exifData.getEditDate());
    
        if(value == 0) return compareDirectoryWith(element);
        return value;
    }
    public int compareUseWith(ImageGridElement element){
        int value = 0;
        if(isFavorite()){
            if(element.isFavorite()){
                value = element.getImageData().getUseCount() - getImageData().getUseCount();
            }else value = -1;
        }else if(element.isFavorite()) value = 1;

        if(value == 0) return compareDirectoryWith(element);
        return value;
    }
    public int compareLastUseTimeWith(ImageGridElement element){
        int value = 0;
        if(isFavorite()){
            if(element.isFavorite()){
                long val = (element.getImageData().getLastUse() - getImageData().getLastUse());
                value = val > 0 ? 1 : (val < 0 ? -1 : 0);
            }else value = -1;
        }else if(element.isFavorite()) value = 1;
        
        if(value == 0) return compareDirectoryWith(element);
        return value;
    }
    public int compareSizeWith(ImageGridElement element){
        int value = Long.compare(element.getExifData().getSize(), exifData.getSize());
    
        if(value == 0) return compareDirectoryWith(element);
        return value;
    }
    public int compareDirectoryWith(ImageGridElement element){
        int value = getImageIdDirectory().compareTo(element.getImageIdDirectory());
    
        if(value == 0) value = getImageIdFileName().compareTo(element.getImageIdFileName());
        return value;
    }
    public int compareNameWith(ImageGridElement element){
        int value = getImageIdFileName().compareTo(element.getImageIdFileName());
        
        if(value == 0) return compareDirectoryWith(element);
        return value;
    }
    
    
    
    // GETTERS / SETTERS
    
    public Image getImage(){
        return image.get();
    }
    public ObjectProperty<Image> imageProperty(){
        return image;
    }
    public void setImage(Image image){
        this.image.set(image);
    }
    public void setRendering(boolean rendering){
        this.rendering = rendering;
    }
    public boolean isRendering(){
        return rendering;
    }
    public ExifUtils.BasicExifData getExifData(){
        return exifData;
    }
    public void setExifData(ExifUtils.BasicExifData exifData){
        this.exifData = exifData;
    }
    public ImageData getLinkedImageData(){
        return linkedImageData;
    }
    public boolean hasLinkedImageData(){
        return linkedImageData != null;
    }
    public boolean isFavorite(){
        return MainWindow.paintTab.favouriteImages.getList().getAllItems().contains(this);
    }
    public void setLinkedImageData(ImageData linkedImageData){
        this.linkedImageData = linkedImageData;
    }
}