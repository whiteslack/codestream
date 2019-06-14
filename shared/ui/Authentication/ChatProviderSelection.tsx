import React from "react";
import Button from "../Stream/Button";
import { Link } from "../Stream/Link";
import { connect } from "react-redux";
import { goToNewUserEntry, goToSignup } from "../store/context/actions";
import { startSSOSignin, SignupType } from "../store/actions";
import { Dispatch } from "../store/common";
import { HostApi } from "../webview-api";

export const ChatProviderSelection = (connect(undefined) as any)(
	(props: { dispatch: Dispatch }) => {
		const onClickSlack = (event: React.SyntheticEvent) => {
			event.preventDefault();
			HostApi.instance.track("Team Type Selected", { "Team Type": "Slack" });
			props.dispatch(startSSOSignin("slack", { type: SignupType.CreateTeam }));
		};

		const onClickMSTeams = (event: React.SyntheticEvent) => {
			event.preventDefault();
			HostApi.instance.track("Team Type Selected", { "Team Type": "MSTeams" });
			props.dispatch(startSSOSignin("msteams", { type: SignupType.CreateTeam }));
		};

		const onClickCodeStream = (event: React.SyntheticEvent) => {
			event.preventDefault();
			HostApi.instance.track("Team Type Selected", { "Team Type": "CodeStream" });
			props.dispatch(goToSignup({ type: SignupType.CreateTeam }));
		};

		const onClickGoBack = (event: React.SyntheticEvent) => {
			event.preventDefault();
			props.dispatch(goToNewUserEntry());
		};

		return (
			<div className="onboarding-page">
				<h2>Create a New Team</h2>
				<form className="standard-form">
					<fieldset className="form-body">
						<h3 style={{ textAlign: "left" }}>Use Existing Slack or Microsoft Teams Channels</h3>
						<p>
							Create a CodeStream team connected to your Slack workspace or Microsoft Teams
							organization so you can discuss code in your existing channels.
						</p>
						<div id="controls">
							<div className="button-group">
								<Button className="control-button" type="button" onClick={onClickSlack}>
									Sign Up with Slack
								</Button>
							</div>
							{false && (
								<div className="button-group">
									<Button className="control-button" type="button" onClick={onClickMSTeams}>
										Sign Up with Microsoft Teams
									</Button>
								</div>
							)}
						</div>
						<br />
						<h3 style={{ textAlign: "left" }}>Use CodeStream Channels</h3>
						<p>Don't use Slack or Microsoft Teams? We'll provide the channels for you!</p>
						<div id="controls">
							<div className="button-group">
								<Button className="control-button" type="button" onClick={onClickCodeStream}>
									Sign Up with CodeStream
								</Button>
							</div>
						</div>
						<br />
						<div id="controls">
							<div className="footer">
								<Link onClick={onClickGoBack}>
									<p>{"< Back"}</p>
								</Link>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		);
	}
);
