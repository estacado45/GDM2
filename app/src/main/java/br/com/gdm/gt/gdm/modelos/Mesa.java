package br.com.gdm.gt.gdm.modelos;

public class Mesa {
    private String mesa,data;
    private boolean status;

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getMesa()+"/"+ getData()+"/"+String.valueOf(isStatus());
    }
}
