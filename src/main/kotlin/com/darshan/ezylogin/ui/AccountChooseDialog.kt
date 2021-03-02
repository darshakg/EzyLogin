package com.darshan.ezylogin.ui

import com.darshan.ezylogin.Credentials
import com.intellij.openapi.ui.ComboBox
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel


class AccountChooseDialog(private val credentialsList: List<Credentials>,
                          private val listener: ListSelectionListener) : JFrame() {

    init {
        setBounds(500, 250, 300, 150)
        val container = contentPane
        container.layout = FlowLayout()
        val namesList: List<String> = getUserNamesList()
        val button = JButton("OK")
        val jLabel = JLabel("Select Account")
        val comboBoxUI = ComboBox<String>(200)
        namesList.forEach {
            comboBoxUI.addItem(it)
        }
        button.addActionListener {
            val index = comboBoxUI.selectedIndex
            listener.onItemSelected(credentialsList[index])
            isVisible = false
            dispose()
        }
        container.add(jLabel)
        container.add(comboBoxUI)
        container.add(button)
    }

    private fun getUserNamesList(): List<String> {
        val namesList: MutableList<String> = mutableListOf()

        credentialsList.forEach {
            namesList.add(it.userName)
        }

        return namesList
    }
}