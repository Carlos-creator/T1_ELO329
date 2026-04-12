import java.util.ArrayList;

public class Territory {
    public void addCellular(Cellular cel){
        cellulars.add(cel);
    }

    public void addTag(EloTelTag tag){
        tags.add(tag);
    }

    public Cellular getCellular(String ownerName){
        //Busca el celular del usuario
        for (Cellular cell: cellulars){
            if (cell.getOwnerName().equals(ownerName)) return cell;
        }
        return null;
    }

    public EloTelTag getTag(String ownerName , String equipmentName){
        //lo mismo pero con el tag
        for (EloTelTag tag: tags){
            if (tag.getOwnerName().equals(ownerName) && tag.getName().equals(equipmentName)) return tag;
        }
        return null;
    }

    private Cellular findNearbyCellular(EloTelTag tag){
        //Revisa hasta encontrar un celular que este cerca del tag
        for (Cellular cell: cellulars) if (tag.isWithinRange(cell)) return cell;
        return null;
    }

    public void forEachTagTryToReportLocation(){
        //Si hay un celular cerca le pide al telefono que actualice la ubicación del tag
        for (EloTelTag tag: tags){
            Cellular nearestCell = findNearbyCellular(tag);
            if (nearestCell != null)
                nearestCell.reportTagLocation(tag);
        }
    }

    

    private ArrayList<Cellular> cellulars = new ArrayList<Cellular>();
    private ArrayList<EloTelTag> tags = new ArrayList<EloTelTag>();
}
