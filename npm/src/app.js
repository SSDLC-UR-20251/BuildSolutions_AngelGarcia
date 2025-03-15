const fs = require('fs');

// Ruta del archivo de transacciones
const RUTA_ARCHIVO = 'transactions.txt';

// Función para leer el archivo transactions.txt
function leerArchivo() {
    try {
        const data = fs.readFileSync(RUTA_ARCHIVO, 'utf8');
        return JSON.parse(data);
    } catch (error) {
        console.error("❌ Error al leer el archivo:", error);
        return {};
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    try {
        fs.writeFileSync(RUTA_ARCHIVO, JSON.stringify(data, null, 4), 'utf8');
        console.log("✅ Transacciones guardadas correctamente.");
    } catch (error) {
        console.error("❌ Error al escribir el archivo:", error);
    }
}

// Función para calcular el saldo actual de un usuario, basado en sus transacciones
function calcularSaldo(usuario) {
    const data = leerArchivo();
    if (!data[usuario]) {
        console.log(`⚠️ El usuario ${usuario} no existe.`);
        return 0;
    }
    return data[usuario].reduce((saldo, transaccion) => saldo + parseFloat(transaccion.balance), 0);
}

// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    let data = leerArchivo();

    // Validaciones
    if (!data[de]) {
        return { exito: false, mensaje: `❌ El usuario remitente (${de}) no existe.` };
    }
    if (!data[para]) {
        return { exito: false, mensaje: `❌ El usuario destinatario (${para}) no existe.` };
    }

    let saldoRemitente = calcularSaldo(de);

    if (saldoRemitente < monto) {
        return { exito: false, mensaje: `❌ Saldo insuficiente. Saldo actual: ${saldoRemitente}` };
    }

    // Registrar transacciones
    const timestamp = new Date().toISOString();
    data[de].push({ balance: -monto, type: "Transfer Out", timestamp });
    data[para].push({ balance: monto, type: "Transfer In", timestamp });

    // Guardar cambios en el archivo
    escribirArchivo(data);
    
    return { exito: true, mensaje: `✅ Transferencia de ${monto} realizada correctamente de ${de} a ${para}.` };
}

// Prueba de transferencia
const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 50);
console.log(resultado.mensaje);

// Exportar funciones para pruebas
module.exports = { leerArchivo, escribirArchivo, calcularSaldo, transferir };
