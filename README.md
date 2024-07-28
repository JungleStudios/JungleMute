# JungleMute

JungleMute è un plugin minecraft che permette di moderare la chat del server, filtrando le parole inappropriate e fornendo comandi per silenziare i giocatori.

## Caratteristiche

- **Filtro parole**: Filtra automaticamente le parole inappropriate nella chat e notifica lo staff.
- **Configurazione**: Personalizza i messaggi e le parole da filtrare tramite file di configurazione.

## Info

- **Compatible Versions**: 1.13 - 1.20.x
- **Tested Versions**: 1.20.x, 1.21

## Screenshots
![image](https://github.com/user-attachments/assets/697c7d6a-2a18-43a3-9d55-a90ef9cfc61a)
![image](https://github.com/user-attachments/assets/b5565bdc-685c-46e4-94e8-b1f303d17f19)
![image](https://github.com/user-attachments/assets/36a92808-9ee8-4044-8be9-b2cd97f0c4f1)



## Installazione

1. Scarica il plugin JungleMute.
2. Copia il file `.jar` nella cartella `plugins` del tuo server Minecraft.
3. Riavvia il server per caricare il plugin.

## Configurazione

Il file di configurazione si trovera in `plugins/JungleMute/config.yml`. Esempio di configurazione:

```yaml
messages:
  player_muted: "&cSei stato silenziato."
  bad_word_alert: "&c%player% ha utilizzato un linguaggio inappropriato: %message%"
  mute_message: "&cClicca qui per silenziare %player%"
  mute_notification: "&e%player% è stato silenziato per %time% per: %motivation%"
  mute_menu_header: "&6Giocatori attualmente silenziati:"
  mute_menu_entry: "&e%player% - %time% rimanenti"
badwords:
  - "parolaccia1"
  - "parolaccia2"
```
## Comandi

- `/jm` - Mostra informazioni sul plugin.
- `/jm reload` - Ricarica la configurazione.
- `/jm info` - Mostra informazioni sul plugin.
- `/jm help` - Mostra i comandi disponibili.
- `/mute <giocatore> <motivo> <tempo> <-s/-p>` - Silenzia un giocatore.

### Esempi di utilizzo

- `/mute Player123 insulti 30m -s` - Silenzia Player123 per 30 minuti in modo silenzioso.
- `/jm reload` - Ricarica la configurazione del plugin.

## Contributi

Siamo aperti a contributi! Se desideri contribuire:

1. Fai un fork del progetto.
2. Crea un branch per la tua feature (`git checkout -b feature/Feature`).
3. Fai il commit delle tue modifiche (`git commit -m 'Add Feature'`).
4. Pusha il branch (`git push origin feature/Feature`).
5. Apri una Pull Request.

Se hai domande o bisogno di supporto, non esitare a contattarci tramite i nostri canali di supporto.
