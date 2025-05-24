package com.example.aondefica;

import android.os.Parcel;
import android.os.Parcelable;

public class Endereco implements Parcelable {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getLocalidade() { return localidade; }
    public String getUf() { return uf; }

    protected Endereco(Parcel in) {
        cep = in.readString();
        logradouro = in.readString();
        complemento = in.readString();
        bairro = in.readString();
        localidade = in.readString();
        uf = in.readString();
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cep);
        parcel.writeString(logradouro);
        parcel.writeString(complemento);
        parcel.writeString(bairro);
        parcel.writeString(localidade);
        parcel.writeString(uf);
    }

    public Endereco() {} // construtor vazio necessário
}
