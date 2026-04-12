public class Cellular extends Equipo {
    public Cellular(String owner, float _x, float _y, ETNube nube){
        super(owner, _x, _y);
        this.nube = nube;
    }


    public void reportTagLocation(EloTelTag tag){
        String tagOwner = tag.getOwnerName();
        String tagName = tag.getName();

        //Reporta a la nube la ubicacion del tag, pero con las coordenadas del celular
        nube.updateLocation(tagOwner, tagName, this.getX(), this.getY());
    }

    private ETNube nube;
}