import com.darshan.ezylogin.Credentials
import com.darshan.ezylogin.LoginComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class LoginConfigForm(private val project: Project): Configurable, DocumentListener {
    private val txtPassword: JTextField = JTextField()
    private val txtUsername: JTextField = JTextField()
    private val addAnotherButton : JButton = JButton()


    private var credentialsList  : MutableList<Credentials> = mutableListOf()

    private var modified = false

    override fun isModified(): Boolean = modified

    override fun getDisplayName(): String ="Easy Login"

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
        val mainPanel = JPanel()
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
        addAnotherButton.setBounds(125, 100, 291, 26)
        mainPanel.add(addAnotherButton)


        addAnotherButton.addActionListener {
            addCredentialsToList()
            txtPassword.text = ""
            txtUsername.text = ""
        }

        txtPassword.document?.addDocumentListener(this)
        txtUsername.document?.addDocumentListener(this)

        return mainPanel
    }

    private fun addCredentialsToList(){
        val userName  = txtUsername.text
        val password = txtPassword.text
        if(userName.isNotEmpty() && password.isNotEmpty()){
            LoginComponent.getInstance(project).state?.credentialsList?.add(Credentials(txtUsername.text,
                    txtPassword.text.toString()))
            LoginComponent.getInstance(project).state?.username = txtUsername.text
        }
    }
}