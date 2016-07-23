package br.com.phcf.financeiro.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContaCorrente.
 */

@Document(collection = "conta_corrente")
public class ContaCorrente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("codigo")
    private String codigo;

    @NotNull
    @Field("saldo")
    private Double saldo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContaCorrente contaCorrente = (ContaCorrente) o;
        if(contaCorrente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contaCorrente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContaCorrente{" +
            "id=" + id +
            ", codigo='" + codigo + "'" +
            ", saldo='" + saldo + "'" +
            '}';
    }
}
