import { ActionType } from "../common";
import * as actions from "./actions";
import { ProvidersState, ProvidersActionsType } from "./types";
import { CodeStreamState } from "..";
import { CSMe } from "@codestream/protocols/api";

type ProviderActions = ActionType<typeof actions>;

const initialState: ProvidersState = {};

export function reduceProviders(state = initialState, action: ProviderActions) {
	switch (action.type) {
		case "RESET":
			return initialState;
		case ProvidersActionsType.Update:
			return { ...state, ...action.payload };
		default:
			return state;
	}
}

export const isConnected = (state: CodeStreamState, providerName: string) => {
	const currentUser = state.users[state.session.userId!] as CSMe;
	const { currentTeamId } = state.context;

	// ensure there's provider info for the team
	if (currentUser.providerInfo == undefined || currentUser.providerInfo[currentTeamId] == undefined)
		return false;

	switch (providerName) {
		case "jiraserver":
		case "github_enterprise": {
			// enterprise/on-prem providers need the `hosts` validated
			const info = currentUser.providerInfo[currentTeamId][providerName];
			return (
				info != undefined &&
				info.hosts != undefined &&
				Object.keys(info.hosts).some(host => state.providers[host] != undefined)
			);
		}
		default:
			// is there an accessToken for the provider?
			const info = currentUser.providerInfo[currentTeamId][providerName];
			return info != undefined && info.accessToken != undefined;
	}
};
