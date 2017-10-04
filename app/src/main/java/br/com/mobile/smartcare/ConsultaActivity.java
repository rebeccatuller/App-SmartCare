package br.com.mobile.smartcare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.mobile.smartcare.modelo.DAO;
import br.com.mobile.smartcare.modelo.Paciente;

public class ConsultaActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fab;
    List<Paciente> pacienteList;
    DAO dao;

    Toolbar toolbar;
    Toolbar searchToolbar;
    Toolbar advancedToolbar;

    EditText searchET;
    Spinner ageSpinner;

    String filtro;
    boolean filtrarNome;
    boolean filtrarIdade;

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("FILTRO", filtro);
        outState.putBoolean("FILTRANOME", filtrarNome);
        outState.putBoolean("FILTRAIDADE", filtrarIdade);
        outState.putInt("SELECTEDINDEX", ageSpinner.getSelectedItemPosition());


        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paciente, menu);
        searchToolbar.inflateMenu(R.menu.menu_search);
        searchToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.action_back) {
                    advancedToolbar.setVisibility(View.GONE);
                    searchToolbar.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    filtrarNome = false;
                    filtrarIdade = false;

                } else if (id == R.id.action_adv) {
                    if (advancedToolbar.getVisibility() == View.GONE) {
                        advancedToolbar.setVisibility(View.VISIBLE);
                        filtrarIdade = true;
                    } else {
                        advancedToolbar.setVisibility(View.GONE);
                        filtrarIdade = false;
                    }
                }
                atualizaLista();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            filtrarNome = true;
            searchToolbar.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
            atualizaLista();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        dao = new DAO(getThis());

        searchET = (EditText) findViewById(R.id.searchET);
        ageSpinner = (Spinner) findViewById(R.id.spinnerFaixa);

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtro = s.toString();
                atualizaLista();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchToolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        advancedToolbar = (Toolbar) findViewById(R.id.toolbarAdvanced);

        //searchToolbar.setNavigationIcon(R.drawable.ic_action_back);

        if(savedInstanceState != null){
            filtro = savedInstanceState.getString("FILTRO");
            filtrarNome = savedInstanceState.getBoolean("FILTRANOME");
            filtrarIdade = savedInstanceState.getBoolean("FILTRAIDADE");
            ageSpinner.setSelection(savedInstanceState.getInt("SELECTEDINDEX"));

            searchET.setText(filtro);

            if(filtrarNome){
                searchToolbar.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();
            }

            if(filtrarIdade){
                advancedToolbar.setVisibility(View.VISIBLE);
            }

            atualizaLista();
        }

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(pacienteList.get(position).getId());


                final Paciente paciente = pacienteList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                builder.setMessage("Paciente de ID " + paciente.getId());

                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dao.deletePaciente(paciente.getId());
                        atualizaLista();
                    }
                });

                builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();

                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.cadastrolayout, (ViewGroup) findViewById(R.id.scrollView));


                        final Spinner spinner;

                        final EditText nomeET;
                        final EditText idadeET;
                        final EditText telefoneET;
                        final EditText doencaET;
                        final EditText leitoET;

                        nomeET = (EditText) layout.findViewById(R.id.nomeET);
                        idadeET = (EditText) layout.findViewById(R.id.idadeET);
                        telefoneET = (EditText) layout.findViewById(R.id.telefoneET);
                        leitoET = (EditText) layout.findViewById(R.id.leitoET);
                        doencaET = (EditText) layout.findViewById(R.id.doencaET);
                        spinner = (Spinner) layout.findViewById(R.id.spinner);


                        nomeET.setText(paciente.getNome());
                        idadeET.setText(paciente.getIdade() + "");
                        telefoneET.setText(paciente.getTelefone());
                        leitoET.setText(paciente.getLeito() + "");
                        doencaET.setText(paciente.getDoenca());
                        String sit = paciente.getSituacao();
                        if (sit.equals("Atendimento")) {
                            spinner.setSelection(0);
                        } else if (sit.equals("Observação")) {
                            spinner.setSelection(1);
                        } else if (sit.equals("Grave")) {
                            spinner.setSelection(2);
                        } else if (sit.equals("Terminal")) {
                            spinner.setSelection(3);
                        }

                        nomeET.setEnabled(false);
                        idadeET.setEnabled(false);
                        doencaET.setEnabled(false);


                        AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                        builder.setView(layout);
                        builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        final AlertDialog dialog2 = builder.create();

                        dialog2.show();
                        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean vazio = false;

                                System.out.println("@@@@@@@@@@@@@@@");
                                String nome = nomeET.getText().toString();
                                String idadeText = idadeET.getText().toString();
                                String telefone = telefoneET.getText().toString();
                                String doenca = doencaET.getText().toString();
                                String leitoText = leitoET.getText().toString();
                                String situacao = String.valueOf(spinner.getSelectedItem());

                                System.out.println(telefone);
                                System.out.println(leitoText);
                                System.out.println(situacao);


                                EditText[] texts = new EditText[]{nomeET, idadeET, telefoneET, doencaET, leitoET};

                                for (EditText et : texts) {
                                    if (et.getText().toString().isEmpty()) {
                                        et.setError("Campo obrigatório");
                                        vazio = true;
                                    }
                                }

                                if (!vazio) {
                                    int idade = Integer.parseInt(idadeText);
                                    int leito = Integer.parseInt(leitoText);

                                    Paciente pacienteUp = new Paciente(paciente.getId(), nome, idade, telefone, leito, doenca, situacao);

                                    dao.updatePaciente(pacienteUp);

                                    Toast.makeText(getThis(), "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                                    System.out.println("insertido");

                                    atualizaLista();
                                    dialog2.dismiss();
                                }
                            }
                        });


                        atualizaLista();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.cadastrolayout, (ViewGroup) findViewById(R.id.scrollView));


                final Spinner spinner;

                final EditText nomeET;
                final EditText idadeET;
                final EditText telefoneET;
                final EditText doencaET;
                final EditText leitoET;

                nomeET = (EditText) layout.findViewById(R.id.nomeET);
                idadeET = (EditText) layout.findViewById(R.id.idadeET);
                telefoneET = (EditText) layout.findViewById(R.id.telefoneET);
                leitoET = (EditText) layout.findViewById(R.id.leitoET);
                doencaET = (EditText) layout.findViewById(R.id.doencaET);
                spinner = (Spinner) layout.findViewById(R.id.spinner);


                AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                builder.setView(layout);
                builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean vazio = false;


                        String nome = nomeET.getText().toString();
                        String idadeText = idadeET.getText().toString();
                        String telefone = telefoneET.getText().toString();
                        String doenca = doencaET.getText().toString();
                        String leitoText = leitoET.getText().toString();
                        String situacao = String.valueOf(spinner.getSelectedItem());

                        EditText[] texts = new EditText[]{nomeET, idadeET, telefoneET, doencaET, leitoET};

                        for (EditText et : texts) {
                            if (et.getText().toString().isEmpty()) {
                                et.setError("Campo obrigatório");
                                vazio = true;
                            }
                        }

                        if (!vazio) {
                            int idade = Integer.parseInt(idadeText);
                            int leito = Integer.parseInt(leitoText);

                            Paciente paciente = new Paciente(nome, idade, telefone, leito, doenca, situacao);


                            dao.insertPaciente(paciente);

                            Toast.makeText(getThis(), "Inserido com sucesso", Toast.LENGTH_SHORT).show();
                            System.out.println("insertido");

                            atualizaLista();
                            dialog.dismiss();
                        }
                    }
                });


                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab)));
        fab.setRippleColor(getResources().getColor(R.color.pressed));


    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaLista();

    }

    public void atualizaLista() {
        listView = (ListView) findViewById(R.id.listView);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        pacienteList = dao.selectPaciente();

        List<Paciente> pacientes = new ArrayList<Paciente>();


        if (filtrarNome) {
            if (filtro != null && !filtro.isEmpty()) {
                for (Paciente paciente : pacienteList) {
                    String nome = paciente.getNome().toLowerCase();
                    String f = filtro.toLowerCase();
                    if (!nome.contains(f)) {
                        pacientes.add(paciente);
                    }
                }
            }
        }

        pacienteList = this.filtraLista(pacienteList, pacientes);
        pacientes = new ArrayList<Paciente>();

        if (filtrarIdade) {
            int faixa = ageSpinner.getSelectedItemPosition();

            if (faixa != 0) {
                int de = 0;
                int para = 0;
                if (faixa == 1) {
                    para = 12;
                } else if (faixa == 2) {
                    de = 12;
                    para = 60;
                } else {
                    de = 60;
                    para = Integer.MAX_VALUE;
                }


                for (Paciente paciente : pacienteList) {
                    if (!(paciente.getIdade() >= de && paciente.getIdade() < para)) {
                        pacientes.add(paciente);
                    }
                }
            }
        }

        pacienteList = this.filtraLista(pacienteList, pacientes);

        for (Paciente paciente : pacienteList) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("nome", paciente.getNome());

            datum.put("id",  String.format(
                    "\nID: %d\nTelefone: %s\nStatus: %s",
                    paciente.getId(),
                    paciente.getTelefone(),
                    paciente.getSituacao()));
            data.add(datum);
        }




        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"nome", "id"}, new int[]{android.R.id.text1, android.R.id.text2});





        listView.setAdapter(adapter);

    }

    private ConsultaActivity getThis() {
        return this;
    }


    private List<Paciente> filtraLista(List<Paciente> in, List<Paciente> filtro) {
        for (Paciente paciente : filtro) {
            if (in.contains(paciente)) {
                in.remove(paciente);
            }
        }

        return in;

    }
}
