package com.example.myapplication


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private val students = mutableListOf<StudentModel>()
    private var lastDeletedStudent: StudentModel? = null
    private var lastDeletedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        students.addAll(getInitialStudentList())
        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            students,
            onEditClick = { position -> showEditDialog(position) },
            onDeleteClick = { position -> showDeleteDialog(position) }
        )

        findViewById<RecyclerView>(R.id.recycler_view_students).apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupAddButton() {
        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddDialog()
        }
    }
    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_student)

        val edtName = dialog.findViewById<EditText>(R.id.edt_student_name)
        val edtId = dialog.findViewById<EditText>(R.id.edt_student_id)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val id = edtId.text.toString()
            if (name.isNotEmpty() && id.isNotEmpty()) {
                val student = StudentModel(name, id)
                students.add(student)
                studentAdapter.notifyItemInserted(students.size - 1)
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(position: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_student)

        val student = students[position]
        val edtName = dialog.findViewById<EditText>(R.id.edt_student_name)
        val edtId = dialog.findViewById<EditText>(R.id.edt_student_id)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

        // Set existing student data
        edtName.setText(student.studentName)
        edtId.setText(student.studentId)

        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val id = edtId.text.toString()
            if (name.isNotEmpty() && id.isNotEmpty()) {
                // Update student
                student.studentName = name
                student.studentId = id
                studentAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getInitialStudentList() = listOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
            .setPositiveButton("Có") { _, _ ->
                deleteStudent(position)
            }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun deleteStudent(position: Int) {
        lastDeletedStudent = students[position]
        lastDeletedPosition = position
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(
            findViewById(android.R.id.content),
            "Đã xóa sinh viên ${lastDeletedStudent?.studentName}",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            lastDeletedStudent?.let {
                students.add(lastDeletedPosition, it)
                studentAdapter.notifyItemInserted(lastDeletedPosition)
            }
        }.show()
    }

}