
const { transferir, leerArchivo, escribirArchivo } = require('../src/app');

beforeEach(() => {
    // Simulamos un estado inicial del archivo transactions.txt
    const dataInicial = {
        "juan.jose@urosario.edu.co": [
            { "balance": "100", "type": "Deposit", "timestamp": "2025-02-11T14:17:21.921536Z" }
        ],
        "sara.palaciosc@urosario.edu.co": [
            { "balance": "200", "type": "Deposit", "timestamp": "2025-03-13T17:41:06.330219Z" }
        ]
    };

    // Escribimos el estado inicial antes de cada prueba
    escribirArchivo(dataInicial);
});

test('Transferencia entre cuentas', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
    expect(resultado.exito).toBe(true);
    expect(resultado.mensaje).toBe('✅ Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co.');

    // Verificamos que los saldos se actualizaron correctamente
    const data = leerArchivo();
    const saldoJuan = data["juan.jose@urosario.edu.co"].reduce((acc, t) => acc + parseFloat(t.balance), 0);
    const saldoSara = data["sara.palaciosc@urosario.edu.co"].reduce((acc, t) => acc + parseFloat(t.balance), 0);

    expect(saldoJuan).toBe(70); // 100 - 30
    expect(saldoSara).toBe(230); // 200 + 30
});

test('Transferencia con saldo insuficiente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('❌ Saldo insuficiente. Saldo actual: 100');

    // Verificamos que los saldos no cambiaron
    const data = leerArchivo();
    const saldoJuan = data["juan.jose@urosario.edu.co"].reduce((acc, t) => acc + parseFloat(t.balance), 0);
    const saldoSara = data["sara.palaciosc@urosario.edu.co"].reduce((acc, t) => acc + parseFloat(t.balance), 0);

    expect(saldoJuan).toBe(100);
    expect(saldoSara).toBe(200);
});

test('Transferencia a usuario inexistente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'usuario.no.existe@urosario.edu.co', 30);
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('❌ El usuario destinatario (usuario.no.existe@urosario.edu.co) no existe.');
});

test('Transferencia desde usuario inexistente', () => {
    const resultado = transferir('usuario.no.existe@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('❌ El usuario remitente (usuario.no.existe@urosario.edu.co) no existe.');
});
