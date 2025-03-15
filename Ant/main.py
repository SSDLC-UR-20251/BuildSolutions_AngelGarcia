import sys
import random
import string

# TODO: Implementar la función para generar códigos de recuperación
def generar_codigo():
    """
    Genera un código de 6 caracteres alfanuméricos sin caracteres ambiguos.
    Caracteres ambiguos a evitar: '0', 'O', '1', 'l'
    """
    # Definir los caracteres válidos (sin caracteres ambiguos)
    caracteres = string.ascii_letters + string.digits
    caracteres_validos = caracteres.replace('0', '').replace('O', '').replace('1', '').replace('l', '')

    # Generar un código de 6 caracteres aleatorios
    codigo = ''.join(random.choice(caracteres_validos) for _ in range(6))
    
    return codigo

if __name__ == "__main__":
    if len(sys.argv) != 2 or not sys.argv[1].isdigit():
        print("Uso: python main.py <cantidad_de_codigos>")
        sys.exit(1)

    cantidad = int(sys.argv[1])
    print("Generando códigos de recuperación...")

    for i in range(cantidad):
        print(f"Código {i+1}: {generar_codigo()}")
