package com.yetu.emscher.mantarepo;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joyent.manta.client.MantaClient;
import com.joyent.manta.client.MantaObject;
import com.joyent.manta.exception.MantaClientHttpResponseException;
import com.joyent.manta.exception.MantaCryptoException;
import com.joyent.manta.exception.MantaObjectException;
import com.yetu.emscher.app.UpdateRepository;
import com.yetu.emscher.app.config.MantaConfig;
import com.yetu.omaha.App;
import com.yetu.omaha.Ping;
import com.yetu.omaha.UpdateCheck;

public class MantaRepo implements UpdateRepository {

	private final static Logger logger = LoggerFactory
			.getLogger(MantaRepo.class);

	private MantaClient manta;

	private String updateBasePath;

	@Inject
	public MantaRepo(MantaConfig mantaConfig) {
		updateBasePath = mantaConfig.getUpdateBasePath();
		try {
			manta = MantaClient.newInstance(mantaConfig.getUrl(),
					mantaConfig.getLogin(), mantaConfig.getPrivateKey(),
					mantaConfig.getKeyFingerprint());
		} catch (IOException e) {
			manta = null;
			logger.error("Error while creating manta client", e);
		}
	}

	public App getUpdateForVersion(App requestApp) {
		String currentVersion = requestApp.getVersion();
		String board = requestApp.getBoard();
		String track = requestApp.getTrack();

		try {
			List<MantaObject> updateFolders = getOrderedListOfUpdateFolder(manta
					.listObjects(getBoardAndTrackSpecificPatch(board, track)));

			MantaObject updateFolder = determineUpdateFolder(currentVersion,
					updateFolders);
			if (updateFolder == null) {
				return createNoUpdate();
			} else {
				return createUpdateResponse(updateFolder);
			}
		} catch (MantaCryptoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MantaClientHttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MantaObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private App createUpdateResponse(MantaObject updateFolder) {
		return null;
	}

	private App createNoUpdate() {
		App app = new App();
		app.setStatus("ok");
		UpdateCheck check = new UpdateCheck();
		check.setStatus("noupdate");
		app.setUpdatecheck(check);
		Ping ping = new Ping();
		ping.setStatus("ok");
		app.setPing(ping);
		return app;
	}

	private MantaObject determineUpdateFolder(String currentVersion,
			List<MantaObject> folders) {
		if (folders.isEmpty()) {
			return null;
		}
		if ("ForcedUpdate".equals(currentVersion)) {
			return folders.get(folders.size() - 1);
		}
		MantaObject nextUpdate = null;
		for (MantaObject o : folders) {
			if (currentVersion.compareTo(o.getPath()) < 0) {
				nextUpdate = o;
				break;
			}
		}
		return nextUpdate;
	}

	private List<MantaObject> getOrderedListOfUpdateFolder(
			Collection<MantaObject> objects) {
		List<MantaObject> folders = objects.stream()
				.filter(o -> o.isDirectory())
				.sorted(new MantaUpdateFolderComparator())
				.collect(Collectors.toList());
		return folders;
	}

	/**
	 * Build a path in the style of {updateBasePath}/{board}/{track}
	 * 
	 * @param board
	 * @param track
	 * @return
	 */
	private String getBoardAndTrackSpecificPatch(String board, String track) {
		StringBuilder builder = new StringBuilder();
		builder.append(updateBasePath);
		if (!updateBasePath.endsWith("/")) {
			builder.append('/');
		}
		builder.append(board);
		builder.append('/');
		builder.append(track);
		builder.append('/');

		return builder.toString();
	}
}
