import java.util.ArrayList;

public class Territory {
    public void addCellular(Cellular cel){
        cellulars.add(cel);
    }

    public void addTag(EloTelTag tag){
        tags.add(tag);
    }

    public void addTablet(Tablet tablet){
        tablets.add(tablet);
    }

    public Cellular getCellular(String ownerName){
        for (Cellular cell: cellulars){
            if (cell.getOwnerName().equals(ownerName)) return cell;
        }
        return null;
    }

    public EloTelTag getTag(String ownerName, String equipmentName){
        for (EloTelTag tag: tags){
            if (tag.getOwnerName().equals(ownerName) && tag.getName().equals(equipmentName)) return tag;
        }
        return null;
    }

    public Tablet getTablet(String ownerName){
        for (Tablet tablet: tablets){
            if (tablet.getOwnerName().equals(ownerName)) return tablet;
        }
        return null;
    }

    private Cellular findNearbyCellular(EloTelTag tag){
        for (Cellular cell: cellulars) if (tag.isWithinRange(cell)) return cell;
        return null;
    }

    public void forEachTagTryToReportLocation(){
        for (EloTelTag tag: tags){
            Cellular nearestCell = findNearbyCellular(tag);
            if (nearestCell != null)
                nearestCell.reportTagLocation(tag);
        }
        for (Tablet tablet: tablets){
            Cellular nearestCell = findNearbyCellular(tablet);
            if (nearestCell != null)
                nearestCell.reportTagLocation(tablet);
        }
    }

    private ArrayList<Cellular> cellulars = new ArrayList<Cellular>();
    private ArrayList<EloTelTag> tags = new ArrayList<EloTelTag>();
    private ArrayList<Tablet> tablets = new ArrayList<Tablet>();
}
