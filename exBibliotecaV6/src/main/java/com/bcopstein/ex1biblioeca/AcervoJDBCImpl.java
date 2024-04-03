package com.bcopstein.ex1biblioeca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Primary
public class AcervoJDBCImpl implements IAcervoRepository{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AcervoJDBCImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Livro> getAll() {
        List<Livro> resp = this.jdbcTemplate.query("SELECT * from livros", (rs, rowNum) -> new Livro(rs.getLong("codigo"),rs.getString("titulo"),rs.getString("autor"),rs.getInt("ano")));
        return resp;
    }

    @Override
    public List<String> getTitulos() {
        return this.jdbcTemplate.queryForList("SELECT titulo FROM livros", String.class);
    }

    @Override
    public List<String> getAutores() {
        return this.jdbcTemplate.queryForList("SELECT DISTINCT autor FROM livros", String.class);
    }

    @Override
    public List<Livro> getLivrosDoAutor(String autor) {
        return this.jdbcTemplate.query("SELECT * FROM livros WHERE autor = ?", new Object[]{autor},
                (rs, rowNum) -> new Livro(rs.getLong("codigo"), rs.getString("titulo"), rs.getString("autor"), rs.getInt("ano")));
    }

    @Override
    public Livro getLivroTitulo(String titulo) {
        List<Livro> livros = this.jdbcTemplate.query("SELECT * FROM livros WHERE titulo = ?", new Object[]{titulo},
                (rs, rowNum) -> new Livro(rs.getLong("codigo"), rs.getString("titulo"), rs.getString("autor"), rs.getInt("ano")));
        return livros.isEmpty() ? null : livros.get(0);
    }

    @Override
    public boolean cadastraLivroNovo(Livro livro) {
        this.jdbcTemplate.update(
                "INSERT INTO livros(codigo,titulo,autor,ano)VALUES(?,?,?,?)",
                livro.getId(),livro.getTitulo(),livro.getAutor(),livro.getAno());
        return true;
    }

    @Override
    public boolean removeLivro(long codigo) {
        String sql = "DELETE FROM livros WHERE id = "+codigo;
        this.jdbcTemplate.batchUpdate(sql);
        return true;
    }
}
