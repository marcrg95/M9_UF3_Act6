package uf3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class BasicFTP {
	public static FTPClient client;
	public static String ServerFTP;

	public static void main(String[] args) {
		// Servidor FTP
		Scanner scan = new Scanner(System.in);

		client = new FTPClient();
		System.out.println("Introdueix IP del servidor FTP: ");
		ServerFTP = scan.nextLine();
		System.out.println("Ens connectem al servidor: " + ServerFTP);

		// Usuari FTP
		System.out.println("Introdueix nom d'usuari");
		String usuari = scan.nextLine();
		System.out.println("Introdueix contrasenya");
		String contrasenya = scan.nextLine();

		try {
			client.connect(ServerFTP);
			boolean login = client.login(usuari, contrasenya);
			if (login) {
				System.out.println("Login correcte... ");
			} else {
				System.out.println("Login incorrecte... ");
				client.disconnect();
				System.exit(1);
			}

			System.out.println("Directori actual: " + client.printWorkingDirectory());
			FTPFile[] files = client.listFiles();
			System.out.println("Fitxers al directori actual: " + files.length);

			// Array par a visualitzar el tipus de fitxer
			String tipus[] = { "Fitxer", "Directori", "Enllaç simbolic" };

			for (int i = 0; i < files.length; i++) {
				System.out.println("\t" + files[i].getName() + "=>" + tipus[files[i].getType()]);
			}

			String comanda = scan.next();
			
			if (comanda.equals("mkdir")) {
				String directori = scan.next();
				creaDirectori(directori);
			} else if (comanda.equals("delete")) {
				String fitxer = scan.next();
				esborraFitxer(fitxer);
			} else if (comanda.equals("put")) {
				String fitxer = scan.next();
				pujaFitxer(fitxer, ServerFTP, usuari, contrasenya);
			}  else if (comanda.equals("rename")) {
				String nom = scan.next();
				String nom2 = scan.next();
				renombraFitxer(nom, nom2);
			}

		} catch (IOException ioe) {

			ioe.printStackTrace();

		}

	}

	public static void creaDirectori(String adreca) {
		try {
			if (client.makeDirectory(adreca)) {
				System.out.println("Directori creat... ");
				client.changeWorkingDirectory(adreca);
			} else {
				System.out.println("No s'ha pogut crear el directori... ");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void esborraFitxer(String adreca) {
		try {
			if (client.deleteFile(adreca)) {
				System.out.println("Fitxer eliminat... ");
			} else {
				System.out.println("No s'ha pogut eliminar el fitxer... ");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void pujaFitxer(String adreca, String Servidor, String usuari, String contrasenya) {
		try {
			System.out.println("Ens connectem al servidor: "+Servidor);
			client.connect(Servidor);
			boolean login = client.login(usuari, contrasenya);
			
			if (login) {

				client.changeWorkingDirectory(adreca);
				client.setFileType(FTP.BINARY_FILE_TYPE);
				
				//Stream d'entrada amb el fitxer que es vol pujar
				BufferedInputStream in = new BufferedInputStream(new FileInputStream("/home/user/text1.txt"));
				client.storeFile("text1.txt", in);
				
				//Stream d'entrada amb el fitxer que es vol pujar
				in = new BufferedInputStream(new FileInputStream("/home/user/world01.png"));
				client.storeFile("world01.png", in);
				
				in.close();
				client.logout();
				client.disconnect();
				System.out.println("L'operació ha acabat... ");
				
			}
			
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
			
		}
	}
	
	public static void renombraFitxer(String nom, String nom2) {
		try {
			if (client.rename(nom, nom2)) {
				System.out.println("Fitxer renombrat... ");
			} else {
				System.out.println("No s'ha pogut renombrar el fitxer... ");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
