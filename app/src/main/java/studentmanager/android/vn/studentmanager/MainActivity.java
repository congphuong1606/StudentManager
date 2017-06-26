package studentmanager.android.vn.studentmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import studentmanager.android.vn.studentmanager.Adapter.StudentAdapter;

public class MainActivity extends AppCompatActivity  {
    final String DATABASE_NAME = "db_student.sqlite";
    SQLiteDatabase database;
    Student student;
    ArrayList<Student> students;
    StudentAdapter studentAdapter;
    ImageButton iBtnCreat,iBtnDelete;
    ListView lvStudents;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        students = new ArrayList<>();
        addControls();
        iBtnCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddStudent.class);
                startActivity(i);
            }
        });
        studentAdapter = new StudentAdapter(this, R.layout.student_row, students);
        lvStudents.setAdapter(studentAdapter);
        readData();
        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = students.get(position);
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("id", student.getId());
                startActivity(i);
            }
        });
        lvStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                checkBox.setVisibility(View.VISIBLE);
                iBtnCreat.setVisibility(View.GONE);
                iBtnDelete.setVisibility(View.VISIBLE);

//                for (Student student: students){
//                    student.setCheck(false);
//                }
                studentAdapter = new StudentAdapter(MainActivity.this, R.layout.student_row, students, false);
                lvStudents.setAdapter(studentAdapter);
                studentAdapter.notifyDataSetChanged();
                return true;
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    studentAdapter = new StudentAdapter(MainActivity.this, R.layout.student_row, students, true);
                    lvStudents.setAdapter(studentAdapter);
                    studentAdapter.notifyDataSetChanged();
                } else {
                    studentAdapter = new StudentAdapter(MainActivity.this, R.layout.student_row, students, false);
                    lvStudents.setAdapter(studentAdapter);
                    studentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from student ", null);
//        cursor.moveToFirst();
        students.clear();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int number = cursor.getInt(2);
                    String address = cursor.getString(3);
                    byte[] image = cursor.getBlob(4);
                    students.add(new Student(id, name, number, address, image));
                } while (cursor.moveToNext());
            }
        }
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            int number = cursor.getInt(2);
//            String address = cursor.getString(3);
//            byte[] image = cursor.getBlob(4);
//            students.add(new Student(id, name, number, address, image));
//        }
        studentAdapter.notifyDataSetChanged();
    }

    private void addControls() {

        iBtnCreat = (ImageButton) findViewById(R.id.ibtn_creat);
        iBtnDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        lvStudents = (ListView) findViewById(R.id.lv_students);
        checkBox= (CheckBox) findViewById(R.id.checkbox);

    }


}
