package Modelo;

import Controlador.Conexion;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class ModeloUsuario {

    Conexion cone = new Conexion();
    Connection cn = cone.iniciarConexion();

    private int doc, sex, rol, Tip;
    private String nom, dir, tec, cor, lo, cl;
    private Date fec;

    public Conexion getCone() {
        return cone;
    }

    public void setCone(Conexion cone) {
        this.cone = cone;
    }

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        this.cn = cn;
    }

    public int getDoc() {
        return doc;
    }

    public void setDoc(int doc) {
        this.doc = doc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getTec() {
        return tec;
    }

    public void setTec(String tec) {
        this.tec = tec;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public Date getFec() {
        return fec;
    }

    public void setFec(Date fec) {
        this.fec = fec;
    }

    public int getTip() {
        return Tip;
    }

    public void setTip(int Tip) {
        this.Tip = Tip;
    }

    public Map<String, Integer> llenarCombo(String sexo) {
        String sql = "Select * from mostrar sexo";
        Map<String, Integer> llenar_combo = new HashMap<>();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                llenar_combo.put(rs.getString(2), rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return llenar_combo;
    }

    public void llenarnuevousuario() {
        Conexion cone = new Conexion();
        Connection cn = cone.iniciarConexion();//instanciamos la conexion

        String sql = "call ins_usuario (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, getDoc());
            ps.setString(2, getNom());
            ps.setString(3, getTec());
            ps.setString(4, getCor());
            ps.setString(5, getDir());
            ps.setDate(6, getFec());
            ps.setInt(7, getSex());
            ps.setInt(8, getRol());
            ps.setString(9, getLo());
            ps.setString(10, getCl());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro almacenado");
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);

        }
        cone.cerrarConexion();
    }

    public void Limpiar(Component[] panel) {
        for (Object control : panel) {
            if (control instanceof JTextField) {
                ((JTextField) control).setText("");

                if (control instanceof JComboBox) {
                    ((JComboBox) control).setSelectedItem("seleccione...");
                }
                if (control instanceof JDateChooser) {
                    ((JDateChooser) control).setDate(null);

                }
            }
        }
    }

    public void mostrarTablaUsuario(JTable tabla, String valor, String nompesta) {
        Conexion conect = new Conexion();
        Connection co = conect.iniciarConexion();

        //Personalizar Emcabezado
        JTableHeader encabeza = tabla.getTableHeader();
        encabeza.setDefaultRenderer(new Gestion_Encabezado());
        tabla.setTableHeader(encabeza);

        //Personalizar Celdas
        tabla.setDefaultRenderer(Object.class, new GestionCeldas());
        JButton editar = new JButton();
        JButton eliminar = new JButton();
//         JButton agregar = new JButton();

        editar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png")));
        eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png")));
//          eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/agregar.png")));

        String[] titulo = {"Documento", "Rol", "Genero", "Nombre", "Telefono", "Correo", "Fecha de nacimiento", "Tipo de Documento", "", ""};
//        int total = titulo.length;
//        if(nompesta.equals("usuario")){
//            titulo=Arrays.copyOf(titulo,titulo.length+2);
//            titulo[titulo.length-1]="";
//        }else{
//            titulo=Arrays.copyOf(titulo,titulo.length+1);
//            titulo[titulo.length-1]="";
//        }
        DefaultTableModel tablaUsuario = new DefaultTableModel(null, titulo) {
            public boolean isCellEditable(int row, int column) {

                return false;

            }
        };

        String sqlusuario;
        if (valor.equals("")) {
            sqlusuario = "SELECT * FROM mostrarusuario";
        } else {
            sqlusuario = "call consultar_usuario('" + valor + "')";
        }
        try {
            String[] dato = new String[titulo.length];
            Statement st = co.createStatement(); //Crea una consulta
            ResultSet rs = st.executeQuery(sqlusuario);
            while (rs.next()) {
                for (int i = 0; i < titulo.length - 2; i++) {
                    dato[i] = rs.getString(i + 1);
                }
                tablaUsuario.addRow(new Object[]{dato[0], dato[1], dato[2], dato[3], dato[4], dato[5], dato[6], dato[7], editar, eliminar});
            }
            co.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabla.setModel(tablaUsuario);
        //Darle Tamaño a cada Columna
        int cantColum = tabla.getColumnCount();
        int[] ancho = {100, 180, 100, 150, 100, 160, 100, 180, 30, 30};
        for (int i = 0; i < cantColum; i++) {
            TableColumn columna = tabla.getColumnModel().getColumn(i);
            columna.setPreferredWidth(ancho[i]);
        }
        conect.cerrarConexion();
    }

    public void BuscarUsuario(int valor) {
        Conexion cone = new Conexion();
        Connection cn = cone.iniciarConexion();
        String sql = "call_mostrarusuario(" + valor + ")";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                setDoc(rs.getInt(1));
                setTip(rs.getInt(2));
                setNom(rs.getString(3));
                setTec(rs.getString(4));
                setCor(rs.getString(5));
                setDir(rs.getString(6));
                setFec(rs.getDate(7));
                setSex(rs.getInt(8));
                setRol(rs.getInt(9));
                setLo(rs.getString(10));
                setCl(rs.getString(11));

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public String obtenerSeleccion(Map<String, Integer> dato, int valor) {
        for (Map.Entry<String, Integer> seleccion : dato.entrySet()) {
            if (seleccion.getValue() == valor) {
                return seleccion.getKey();
            }
        }
        return null;
    }

    public void EliminarUsuario() {
        Conexion cone = new Conexion();
        Connection cn = cone.iniciarConexion();
        System.out.println(getDoc());
        String eliUsuario = "call usuario_eli(?)";
        try {
            PreparedStatement ps = cn.prepareStatement(eliUsuario);
            ps.setInt(1, getDoc());
            ps.executeLargeUpdate();
            Icon Icon = new ImageIcon(getClass().getResource("/img/eliminar(2).png"));
//    JOptionPane.showMessageDialog(null, "Registro Eliminado", "Eliminar Usuario", JOptionPane.PLAIN_MESSAGE (Icon)"eliminar");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void mostrarTablaUsuario() {
    }
}
