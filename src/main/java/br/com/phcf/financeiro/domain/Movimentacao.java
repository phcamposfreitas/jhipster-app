package br.com.phcf.financeiro.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import br.com.phcf.financeiro.domain.enumeration.TipoMovimentacao;

/**
 * A Movimentacao.
 */

@Document(collection = "movimentacao")
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("valor")
    private Double valor;

    @Field("tipo")
    private TipoMovimentacao tipo;

    @Field("data")
    private LocalDate data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movimentacao movimentacao = (Movimentacao) o;
        if(movimentacao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movimentacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
            "id=" + id +
            ", valor='" + valor + "'" +
            ", tipo='" + tipo + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
