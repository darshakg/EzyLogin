package com.darshan.ezylogin

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.filechooser.FileNameExtensionFilter

class LoginConfigForm(private val project: Project) : Configurable, DocumentListener {
    private val txtPassword: JTextField = JTextField()
    private val txtUsername: JTextField = JTextField()
    private val addAnotherButton: JButton = JButton()
    private val clearStoredAccounts: JButton = JButton()
    private val importFromCSV: JButton = JButton()
    private val exportToCSV: JButton = JButton()
    private val mainPanel = JPanel()

    private var modified = false

    override fun isModified(): Boolean = modified

    override fun getDisplayName(): String = "Easy Login"

    override fun apply() {
        addCredentialsToList()
        modified = false
    }

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun insertUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun createComponent(): JComponent {
        mainPanel.setBounds(0, 0, 452, 254)
        mainPanel.layout = null
        val lblUsername = JLabel("UserName")
        lblUsername.setBounds(30, 25, 83, 16)
        mainPanel.add(lblUsername)

        val lblPassword = JLabel("Password")
        lblPassword.setBounds(30, 74, 83, 16)
        mainPanel.add(lblPassword)

        txtUsername.setBounds(125, 20, 291, 26)
        txtUsername.columns = 10
        mainPanel.add(txtUsername)

        txtPassword.setBounds(125, 69, 291, 26)
        mainPanel.add(txtPassword)

        addAnotherButton.text = "Add Account Credentials"
        addAnotherButton.setBounds(125, 110, 291, 30)
        mainPanel.add(addAnotherButton)

        clearStoredAccounts.text = "Clear Stored Accounts"
        clearStoredAccounts.setBounds(125, 150, 291, 30)
        mainPanel.add(clearStoredAccounts)

        importFromCSV.text = "Import Credentials From CSV"
        importFromCSV.setBounds(125, 190, 291, 30)
        mainPanel.add(importFromCSV)

        exportToCSV.text = "Export Credentials to CSV"
        exportToCSV.setBounds(125, 230, 291, 30)
        mainPanel.add(exportToCSV)


        addAnotherButton.addActionListener {
            addCredentialsToList()
            txtPassword.text = ""
            txtUsername.text = ""
        }

        clearStoredAccounts.addActionListener {
            clearStoredCredentials()
        }

        importFromCSV.addActionListener {
            importCredentialsFromCSV()
        }

        exportToCSV.addActionListener {
            exportCredentialsToCSV()
        }

        txtPassword.document?.addDocumentListener(this)
        txtUsername.document?.addDocumentListener(this)

        return mainPanel
    }

    private fun addCredentialsToList() {
        val userName = txtUsername.text
        val password = txtPassword.text
        if (userName.isNotEmpty() && password.isNotEmpty()) {
            LoginComponent.getInstance().state?.credentialsList?.let {
                it.add(
                    Credentials(
                        txtUsername.text.trim(),
                        txtPassword.text.trim()
                    )
                )
//                JsonFileUtil.insertCredentialListToJsonFile(project,it)
                JOptionPane.showMessageDialog(mainPanel, "Username : $userName added successfully",
                    "Success", JOptionPane.PLAIN_MESSAGE)
            }
            LoginComponent.getInstance().state?.username = txtUsername.text
        }else{
            JOptionPane.showMessageDialog(mainPanel, "Please fill in the fields",
                "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun importCredentialsFromCSV() {
        val fileChooser = JFileChooser()
        val fileFilter = FileNameExtensionFilter("CSV file", "csv")
        fileChooser.fileFilter = fileFilter
        val res = fileChooser.showOpenDialog(mainPanel)
        if (res == JFileChooser.APPROVE_OPTION) {
            val bufferedReader = Files.newBufferedReader(Paths.get(fileChooser.selectedFile.toString()))
            val csvParser = CSVParser(
                bufferedReader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
            )
            for (csvRecord in csvParser) {
                val userName = csvRecord.get("Username")
                val password = csvRecord.get("Password")
                if (userName.isNotEmpty() && password.isNotEmpty()) {
                    LoginComponent.getInstance().state?.credentialsList?.add(
                        Credentials(
                            userName,
                            password
                        )
                    )
                    LoginComponent.getInstance().state?.username = userName
                }
            }
            JOptionPane.showMessageDialog(mainPanel, "${fileChooser.selectedFile} file imported successfully",
                "Import successful", JOptionPane.PLAIN_MESSAGE)
        }
    }

    private fun exportCredentialsToCSV() {
        if (LoginComponent.getInstance().state?.credentialsList?.isEmpty() == true){
            JOptionPane.showMessageDialog(
                mainPanel, "No Accounts to login please configure and add accounts in plugin settings",
                "Error", JOptionPane.ERROR_MESSAGE
            )
        }else {
            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = "Export As CSV File"
            fileChooser.selectedFile = File("Untitled.csv")
            fileChooser.fileFilter = FileNameExtensionFilter("CSV", "csv")
            val result = fileChooser.showSaveDialog(mainPanel)
            if (result == JFileChooser.APPROVE_OPTION) {
                val writer = Files.newBufferedWriter(Paths.get(fileChooser.selectedFile.toString()))
                val csvPrinter = CSVPrinter(
                    writer, CSVFormat.DEFAULT
                        .withHeader("Username", "Password")
                )
                LoginComponent.getInstance().state?.credentialsList?.let { credentialList ->
                    credentialList.forEach {
                        csvPrinter.printRecord(it.userName, it.password)
                    }
                }
                csvPrinter.flush()
                csvPrinter.close()
                JOptionPane.showMessageDialog(
                    mainPanel, "File exported to location ${fileChooser.selectedFile}",
                    "Export successful", JOptionPane.PLAIN_MESSAGE
                )
            }
        }
    }

    private fun clearStoredCredentials() {
        LoginComponent.getInstance().state?.apply {
            credentialsList.clear()
            previouslyLoggedInIndex = -1
//            JsonFileUtil.clearCredentialFile(project)
            JOptionPane.showMessageDialog(
                mainPanel, "All Login Accounts are cleared",
                "Success", JOptionPane.PLAIN_MESSAGE
            )
        }
    }
}
